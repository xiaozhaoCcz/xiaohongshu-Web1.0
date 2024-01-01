import { defineStore } from "pinia";
import { ElMessage, ElNotification } from "element-plus";
import { appConfig, avatarPrefix, generateToken } from "@/utils/zimutil";
import {
  ZIM,
  ZIMConversation,
  ZIMEventOfConversationChangedResult,
  ZIMGroupMemberInfo,
  ZIMMessage,
  ZIMMessageReceiptInfo,
} from "zego-zim-web";

export * from "zego-zim-web";

const store = defineStore("zimStore", {
  state: () =>
    ({
      isLogged: false,
      user: {
        userID: "",
        userName: "",
        memberAvatarUrl: "",
        extendedData: "",
      },
      userMap: {} as Record<string, ZIMGroupMemberInfo>,
      msgReceiptMap: {} as Record<string, ZIMMessageReceiptInfo>,
      convMap: new Map<string, ZIMConversation>(),
      totalMemberCount: 0,
      totalUnreadMessageCount: 0,
      convs: [],
      groupList: [],
      callList: [],
      memberList: [],
      msgList: [],
      convInfo: {
        conversationID: "",
        conversationName: "",
        conversationAvatarUrl: "",
        type: 0,
        unreadMessageCount: 0,
        orderKey: 0,
        notificationStatus: 0,
        lastMessage: null,
        notice: "",
        receiptMsgID: "",
      },
      callInfo: {
        callID: "",
        caller: "",
        state: -1, // call state
        mode: 0,
        userStateMap: {} as Record<string, number>, // userID-state
        createTime: 0,
        acceptTime: 0,
        quitTime: 0,
        endTime: 0,
        isShow: false,
        selfState: -1, // self user state
        isMultiDevice: false,
      },
      appGlobalConfig: {
        resourcesID: "",
        voIPConfig: { iOSVoIPHasVideo: false, iOSVoIPHandleType: 1, iOSVoIPHandleValue: "" },
      },
    }) as any,
  actions: {
    initEvent() {
      const noop = () => {};
      zim.on("error", noop);
      zim.on("tokenWillExpire", noop);
      zim.on("messageSentStatusChanged", noop);
      zim.on("groupMemberInfoUpdated", noop);
      zim.on("roomAttributesBatchUpdated", noop);
      zim.on("roomMemberAttributesUpdated", noop);

      // Main
      zim.on("connectionStateChanged", (zim: any, data) => {
        if (data.state == 0) this.isLogged = false;
      });
      zim.on("userInfoUpdated", (zim: any, data) => {
        this.user.userName = data.info.baseInfo.userName;
        this.user.memberAvatarUrl = data.info.userAvatarUrl;
        this.user.extendedData = data.info.extendedData;
      });

      // Message
      const onMessageReceived = (
        messageList: ZIMMessage[],
        fromConversationID: string,
        fromConversationType: number
      ) => {
        console.log("开始接收消息");
        if (fromConversationID == this.convInfo.conversationID && fromConversationType == this.convInfo.type) {
          messageList.forEach((item) => {
            if (this.msgList.find((msg: any) => msg.msg.messageID == item.messageID)) return;
            const msg: any = { msg: { ...item }, ext: { _time: "" } };
            try {
              if (item.type == 200) {
                msg.custom = JSON.parse(item.message as string);
                msg.custom._values = [];
              }
            } catch (error) {}
            this.msgList.push(msg);
          });
        }
      };
      zim.on("receivePeerMessage", (zim: any, data) => {
        onMessageReceived(data.messageList, data.fromConversationID, 0);
      });
      zim.on("receiveGroupMessage", (zim: any, data) => {
        onMessageReceived(data.messageList, data.fromConversationID, 2);
      });
      zim.on("receiveRoomMessage", (zim: any, data) => {
        onMessageReceived(data.messageList, data.fromConversationID, 1);
      });
      zim.on("broadcastMessageReceived", (zim: any, { message }) => {
        // ts-ignore
        const str = message.message || message.fileName || message.fileDownloadUrl;
        ElNotification({ title: "Broadcast message", message: `Type: ${message.type}, content: ${str}` });
      });
      zim.on("messageDeleted", (zim: any, data) => {
        if (data.conversationID == this.convInfo.conversationID && data.conversationType == this.convInfo.type) {
          this.msgList.length = 0;
          if (!data.isDeleteConversationAllMessage) {
            this.msgList.forEach((msg: any) => {
              const id = msg.msg.messageID;
              if (data.messageList.every((item) => item.messageID != id)) {
                this.msgList.push(msg);
              }
            });
          }
        }
      });
      zim.on("messageReceiptChanged", (zim: any, data) => {
        const map = { ...this.msgReceiptMap };
        data.infos.forEach((item) => (map[item.messageID] = item));
        this.msgReceiptMap = map;
      });
      zim.on("messageRevokeReceived", (zim: any, data) => {
        data.messageList.forEach((revokeMsg) => {
          const index = this.msgList.findIndex((item: any) => item.msg.messageID == revokeMsg.messageID);
          if (index != -1) {
            this.msgList.splice(index, 1, { msg: { ...revokeMsg } });
          }
        });
      });
      zim.on("messageReactionsChanged", (zim: any, data) => {
        data.reactions.forEach((item) => {
          const msgObj = this.msgList.find((obj: any) => obj.msg.messageID == item.messageID);
          if (!msgObj) return;
          const reactions = msgObj.msg.reactions || [];
          const index = reactions.findIndex((_reaction: any) => _reaction.reactionType == item.reactionType);
          if (index !== -1) {
            if (item.totalCount == 0) {
              reactions.splice(index, 1);
            } else {
              reactions.splice(index, 1, item);
            }
          } else {
            reactions.push(item);
          }
          msgObj.msg.reactions = reactions;
        });
      });

      // Conversation
      zim.on("conversationChanged", (zim: any, data) => {
        this.conversationChanged(data);
      });
      zim.on("conversationsAllDeleted", (zim: any) => {
        this.conversationChanged();
      });
      zim.on("conversationTotalUnreadMessageCountUpdated", (zim: any, data) => {
        this.totalUnreadMessageCount = data.totalUnreadMessageCount;
        this.queryConversationList();
      });
      zim.on("conversationMessageReceiptChanged", (zim, data) => {
        data.infos.some((item) => {
          if (item.conversationID == this.convInfo.conversationID && item.conversationType == this.convInfo.type) {
            this.convInfo.receiptMsgID = item.messageID;
            return true;
          }
        });
      });

      // Group
      zim.on("groupStateChanged", (zim: any, data) => {
        if (!this.isLogin) return;

        const message = `event: ${groupStateTips[data.event]}, operator: ${data.operatedInfo.userID}`;
        this.insertMessageToLocalDB({ type: 1, message }, data.groupInfo.baseInfo.groupID, data.operatedInfo.userID);
      });
      zim.on("groupMemberStateChanged", (zim: any, data) => {
        if (data.groupID == this.convInfo.conversationID && this.convInfo.type == 2) {
          this.queryGroupMemberList();
        }
        const ids = data.userList.map((v) => v.userID).join();
        const message = `event: ${groupMemberStateTips[data.event]}, operator: ${
          data.operatedInfo.userID
        }, ids: ${ids}`;
        this.insertMessageToLocalDB({ type: 1, message }, data.groupID, data.operatedInfo.userID);
      });
      zim.on("groupNameUpdated", (zim: any, data) => {
        const { groupID, groupName } = data;
        if (groupID == this.convInfo.conversationID && this.convInfo.type == 2) {
          this.convInfo.conversationName = groupName;
        }
        const message = `${data.operatedInfo.userID} updated group name`;
        this.insertMessageToLocalDB({ type: 1, message }, data.groupID, data.operatedInfo.userID);
      });
      zim.on("groupAvatarUrlUpdated", (zim: any, data) => {
        const message = `${data.operatedInfo.userID} updated group avatar url`;
        this.insertMessageToLocalDB({ type: 1, message }, data.groupID, data.operatedInfo.userID);
      });
      zim.on("groupNoticeUpdated", (zim: any, data) => {
        if (data.groupID == this.convInfo.conversationID && this.convInfo.type == 2) {
          this.convInfo.notice = data.groupNotice;
        }
        const message = `${data.operatedInfo.userID} updated group notice`;
        this.insertMessageToLocalDB({ type: 1, message }, data.groupID, data.operatedInfo.userID);
      });
      zim.on("groupAttributesUpdated", (zim: any, data) => {
        if (data.groupID == this.convInfo.conversationID && this.convInfo.type == 2) {
          this.convInfo.note = data.infoList[0].groupAttributes.remark;
        }
        const message = `${data.operatedInfo.userID} updated group attributes`;
        this.insertMessageToLocalDB({ type: 1, message }, data.groupID, data.operatedInfo.userID);
      });

      // Room
      zim.on("roomMemberJoined", (zim: any, data) => {
        if (data.roomID == this.convInfo.conversationID && this.convInfo.type == 1) {
          this.queryRoomMember();
          this.queryRoomOnlineMemberCount(data.roomID);
        }
      });
      zim.on("roomMemberLeft", (zim: any, data) => {
        if (data.roomID == this.convInfo.conversationID && this.convInfo.type == 1) {
          this.queryRoomMember();
          this.queryRoomOnlineMemberCount(data.roomID);
        }
      });
      zim.on("roomAttributesUpdated", (zim: any, data) => {
        if (data.roomID == this.convInfo.conversationID && this.convInfo.type == 1) {
          this.convInfo.notice = data.infos[0].roomAttributes.RoomNotice;
        }
      });
      const roomDisconnected = [3, 8, 9, 10];
      zim.on("roomStateChanged", (zim: any, data) => {
        if (
          data.roomID == this.convInfo.conversationID &&
          this.convInfo.type == 1 &&
          data.state == 0 &&
          roomDisconnected.includes(data.event)
        ) {
          this.convInfo.conversationID = "";
        }
      });
    },

    setAppGlobalConfig(conf: any, init = false) {
      this.appGlobalConfig.resourcesID = conf.resourcesID;
      this.appGlobalConfig.voIPConfig = { ...conf.voIPConfig };
      init && conf.geoFence.type && ZIM.setGeofencingConfig(conf.geoFence.type, conf.geoFence.areas);
    },

    login(payload: any) {
      document.title = `ZIM-${SDKVersion}-${payload.userID}`;
      localStorage.setItem("ZIMDEMOUSER", JSON.stringify(payload));
      this.initEvent();
      this.user = payload;
      return zim.login(payload, generateToken(payload.userID, 0)).then((res) => {
        this.isLogged = true;
        // query self info
        zim.queryUsersInfo([this.user.userID], { isQueryFromServer: true }).then((res) => {
          const user = {
            ...res.userList[0].baseInfo,
            memberAvatarUrl: res.userList[0].userAvatarUrl,
            extendedData: res.userList[0].extendedData,
          };
          this.user = user;
          this.userMap[this.user.userID] = user;
        });
        return res;
      });
    },

    logout(isSend = true) {
      this.isLogged = false;
      return isSend && zim.logout();
    },

    setUserInfo(data: any) {
      if (data.name) {
        zim.updateUserName(data.name).then(() => (this.user.userName = data.name));
      }
      let avatar = data.avatar;
      if (avatar) {
        avatar = avatarPrefix + avatar;
        zim.updateUserAvatarUrl(avatar).then(() => {
          this.userMap[this.user.userID] = { ...this.userMap[this.user.userID], memberAvatarUrl: avatar };
          this.user.memberAvatarUrl = avatar;
        });
      }
      if (data.note) {
        zim.updateUserExtendedData(data.note).then(() => (this.user.extendedData = data.note));
      }
    },

    setUserMap(ids: string[]) {
      if (!ids || !ids.length) return;
      zim.queryUsersInfo(ids, { isQueryFromServer: true }).then((res) => {
        res.userList.forEach((item) => {
          this.userMap[item.baseInfo.userID] = {
            ...item.baseInfo,
            memberAvatarUrl: item.userAvatarUrl,
            extendedData: item.extendedData,
          };
        });
      });
    },

    queryConversationList() {
      return zim.queryConversationList({ count: 1000 }).then((res) => {
        res.conversationList.sort((a: ZIMConversation, b: ZIMConversation) => {
          if (a.isPinned !== b.isPinned) {
            return b.isPinned ? 1 : -1;
          } else {
            return b.orderKey - a.orderKey;
          }
        });
        this.convs = res.conversationList;
        const ids: string[] = [];
        const map = new Map();
        res.conversationList.forEach((item) => {
          if (item.type == 0) {
            if (!item.conversationName || !item.conversationAvatarUrl) {
              ids.push(item.conversationID);
            } else {
              this.userMap[item.conversationID] = {
                ...this.userMap[item.conversationID],
                userID: item.conversationID,
                userName: item.conversationName,
                memberAvatarUrl: item.conversationAvatarUrl,
              };
            }
          }
          map.set(item.type + item.conversationID, item);
        });
        this.convMap = map;
        this.setUserMap(ids);
        return res;
      });
    },

    conversationChanged(data: ZIMEventOfConversationChangedResult) {
      if (!data) {
        this.convInfo.conversationID = "";
        this.convs = [];
        this.convMap.clear();
        return;
      }

      data.infoList.forEach((item) => {
        const key = item.conversation.type + item.conversation.conversationID;
        if (item.event == 3) {
          this.convMap.delete(key);
        } else {
          this.convMap.set(key, item.conversation);
        }
      });
      const convs: ZIMConversation[] = Array.from(this.convMap.values());
      convs.sort((a, b) => {
        if (a.isPinned !== b.isPinned) {
          return b.isPinned ? 1 : -1;
        } else {
          return b.orderKey - a.orderKey;
        }
      });
      this.convs = convs;
    },

    updateMessageReceipt(msg: any, count: number, info: any) {
      if (info) info.readMemberCount = count;
      else {
        info = {
          conversationID: msg.conversationID,
          conversationType: msg.conversationType,
          messageID: msg.messageID,
          status: msg.receiptStatus,
          readMemberCount: count,
          unreadMemberCount: -1,
        };
        this.msgReceiptMap = { ...this.msgReceiptMap, [msg.messageID]: info };
      }
    },

    queryHistoryMessage(convID: string, type: number, config: any) {
      this.msgList.length = 0;
      config = {
        count: 1000,
        reverse: true,
        ...config,
      };
      zim.queryHistoryMessage(convID, type, config).then((res) => {
        res.messageList.forEach((item) => {
          const msg: any = { msg: { ...item }, ext: { _time: "" } };
          try {
            if (item.type == 200) {
              msg.custom = JSON.parse(item.message as string);
              msg.custom._values = [];
            }
          } catch (error) {}
          this.msgList.push(msg);
        });
      });
    },

    sendMessage(text: string, convID: string, convType: number, hasReceipt = true, isByte = false) {
      const time = new Date().toLocaleString();
      const isBarrage = convType == 1 && hasReceipt ? true : false;
      const content = isByte
        ? new Uint8Array(Array.from(unescape(encodeURIComponent(text))).map((c) => c.charCodeAt(0)))
        : text;

      const msgObj = {
        message: content,
        type: isByte ? 2 : isBarrage ? 20 : 1,
        extendedData: time,
        // localExtendedData: time,
      };
      const pushConfig =
        msgObj.type != 1 || convType == 1
          ? void 0
          : {
              title: "Received [Text] message from " + convID,
              content: text,
              payload: "payload",
              resourcesID: this.appGlobalConfig.resourcesID,
              voIPConfig: this.appGlobalConfig.voIPConfig,
            };

      zim
        .sendMessage(msgObj, convID, convType, {
          priority: 2,
          pushConfig,
          hasReceipt: msgObj.type == 1 && convType != 1 ? hasReceipt : false,
        })
        .then(() => {
          console.log("sendMessage", { convID, convType, msgObj });
          this.msgList.push({
            msg: { ...msgObj },
            ext: { _time: "" },
          });
        })
        .catch((error) => {
          console.log("sendMessage", { convID, convType, msgObj, error });
          this.msgList.push({
            msg: { ...msgObj },
            ext: { _time: "" },
          });
          ElMessage.error(`code: ${error.code}, message: ${error.message}`);
        });
    },

    sendMediaMessage(msgObj: any, convID: string, convType: number, hasReceipt: boolean) {
      hasReceipt = convType == 1 ? false : hasReceipt;
      const pushConfig =
        convType == 1
          ? void 0
          : {
              title: "Received [Media] message from " + convID,
              content: msgObj.fileLocalPath?.name || msgObj.fileDownloadUrl || "Media File Name",
              payload: "payload",
              resourcesID: this.appGlobalConfig.resourcesID,
              voIPConfig: this.appGlobalConfig.voIPConfig,
            };
      return zim
        .sendMediaMessage(
          msgObj,
          convID,
          convType,
          { priority: 2, pushConfig, hasReceipt },
          {
            onMessageAttached: () =>
              this.msgList.push({
                msg: { ...msgObj },
                ext: { _time: "" },
              }),
            onMediaUploadingProgress: (msg, cur: number, total: number) =>
              console.log("onMediaUploadingProgress", ((100 * cur) / total).toFixed(2)),
          }
        )
        .then(() => {
          console.log("sendMediaMessage", { msgObj, convID, convType });
          const index = this.msgList.findIndex((item: any) => item.msg.localMessageID == msgObj.localMessageID);
          this.msgList.splice(index, 1, {
            msg: { ...msgObj },
            ext: { _time: "" },
          });
        })
        .catch((error) => {
          console.log("sendMediaMessage", { msgObj, convID, convType, error });
          const index = this.msgList.findIndex((item: any) => item.msg.localMessageID == msgObj.localMessageID);
          this.msgList.splice(index, 1, {
            msg: { ...msgObj },
            ext: { _time: "" },
          });
          ElMessage.error(`code: ${error.code}, message: ${error.message}`);
        });
    },

    sendCustomMessage(msgObj: any, convID: string, convType: number, hasReceipt: boolean) {
      hasReceipt = convType == 1 ? false : hasReceipt;
      const pushConfig =
        convType == 1
          ? void 0
          : {
              title: "Received [Custom] message from " + convID,
              content: msgObj.searchedContent,
              payload: "payload",
              resourcesID: this.appGlobalConfig.resourcesID,
              voIPConfig: this.appGlobalConfig.voIPConfig,
            };
      const custom = JSON.parse(msgObj.message);
      custom._values = [];

      zim
        .sendMessage(msgObj, convID, convType, { priority: 2, pushConfig, hasReceipt })
        .then(() => {
          console.log("sendMessage", { msgObj, convID, convType });
          this.msgList.push({
            msg: { ...msgObj },
            ext: { _time: "" },
            custom,
          });
        })
        .catch((error) => {
          console.log("sendMessage", { msgObj, convID, convType, error });
          this.msgList.push({
            msg: { ...msgObj },
            ext: { _time: "" },
            custom,
          });
          ElMessage.error(`code: ${error.code}, message: ${error.message}`);
        });
    },

    insertMessageToLocalDB(msg: any, groupID: string, sender: string) {
      const conv = this.convs.find((item: any) => item.conversationID == groupID && item.type == 2);
      if (!conv) return;

      zim.insertMessageToLocalDB(msg, groupID, 2, sender).then((res) => {
        if (groupID == this.convInfo.conversationID && this.convInfo.type == 2) {
          this.msgList.push({ msg: { ...res.message } });
        }
      });
    },

    ackMessageReceipt(msg: any) {
      zim.sendMessageReceiptsRead([msg], msg.conversationID, msg.conversationType).then(() => {
        msg.receiptStatus = 2;
      });
    },

    queryGroupAttributes(groupID: string, keys: any) {
      if (keys?.length) {
        return zim.queryGroupAttributes(keys, groupID);
      }

      return zim.queryGroupAllAttributes(groupID);
    },

    createRoom(roomInfo: any, config: any, isCreate: boolean) {
      const { roomID, roomName } = roomInfo;
      if (isCreate) {
        return zim.createRoom({ roomID, roomName }, config);
      } else {
        return zim.enterRoom({ roomID, roomName }, config);
      }
    },

    leaveRoom() {
      zim.leaveRoom(this.convInfo.conversationID).then(() => {
        this.convInfo.conversationID = "";
      });
    },

    queryRoomAllAttributes(roomID: string) {
      return zim.queryRoomAllAttributes(roomID).then((res) => {
        if (roomID == this.convInfo.conversationID && this.convInfo.type == 1 && res.roomAttributes.RoomNotice) {
          this.convInfo.notice = res.roomAttributes.RoomNotice;
        }
      });
    },

    getGroupList() {
      return zim.queryGroupList().then((res) => {
        this.groupList.length = 0;
        this.groupList.push(...res.groupList);
        return res;
      });
    },

    updateGroupInfo(groupID: string, groupName: string, notice: string, avatar: string, attr: string) {
      if (groupName) zim.updateGroupName(groupName, groupID).then(() => (this.convInfo.conversationName = groupName));

      if (notice) zim.updateGroupNotice(notice, groupID).then(() => (this.convInfo.notice = notice));

      if (avatar)
        zim
          .updateGroupAvatarUrl(avatarPrefix + avatar, groupID)
          .then(() => (this.convInfo.conversationAvatarUrl = avatar));

      if (attr) zim.setGroupAttributes({ remark: attr }, groupID).then(() => (this.convInfo.note = attr));
    },

    updateMemberInfo(groupID: string, userID: string, role: number, nickname: string) {
      if (!userID) return;

      if (role) zim.setGroupMemberRole(role, userID, groupID).then(() => this.queryGroupMemberList(groupID));
      if (nickname) zim.setGroupMemberNickname(nickname, userID, groupID);
    },

    transferGroupOwner(groupID: string, userID: string) {
      return zim.transferGroupOwner(userID, groupID).then(() => this.queryGroupMemberList(groupID));
    },

    queryGroupMemberList(groupID = "", config: any = {}) {
      groupID = groupID || this.convInfo.conversationID;
      const _config = {
        nextFlag: 0,
        count: 100,
        ...config,
      };
      if (!_config.nextFlag) {
        this.memberList.length = 0;
        this.queryGroupMemberCount(groupID);
      }
      return zim.queryGroupMemberList(groupID, _config).then((res) => {
        res.userList.forEach((item: { userID: string | number }) => {
          this.userMap[item.userID] = { ...item };
          if (!this.memberList.find((member: any) => member.userID == item.userID)) {
            this.memberList.push(item);
          }
        });

        if (res.nextFlag) {
          this.queryGroupMemberList(groupID, { nextFlag: res.nextFlag });
        }
      });
    },

    queryGroupMemberCount(groupID = "") {
      groupID = groupID || this.convInfo.conversationID;
      return zim.queryGroupMemberCount(groupID).then((res) => {
        this.totalMemberCount = res.count;
      });
    },

    deleteConversation(conv: any) {
      const config = { isAlsoDeleteServerConversation: true };
      return zim.deleteConversation(conv.conversationID, conv.type, config).then(() => {
        const index = this.convs.findIndex((item: any) => item == conv);
        index != -1 && this.convs.splice(index, 1);
        if (this.convs.length == 0) this.convInfo.conversationID = "";
      });
    },

    deleteAllConversations(config: any) {
      return zim.deleteAllConversations(config).then(() => {
        this.convInfo.conversationID = "";
        this.convs = [];
        this.convMap.clear();
      });
    },

    clearAllConversationsUnreadMessageCount() {
      return zim.clearAllConversationsUnreadMessageCount().then(() => {
        this.queryConversationList();
      });
    },

    leaveGroup(groupID: string) {
      return zim.leaveGroup(groupID).then((res) => {
        const index = this.groupList.findIndex((item: any) => item.groupID == groupID);
        index !== -1 && this.groupList.splice(index, 1);
        return res;
      });
    },

    dismissGroup(groupID: string) {
      return zim.dismissGroup(groupID).then((res) => {
        const index = this.groupList.findIndex((item: any) => item.groupID == groupID);
        index !== -1 && this.groupList.splice(index, 1);
        return res;
      });
    },

    queryRoomMember(roomID = "", config: any = {}) {
      roomID = roomID || this.convInfo.conversationID;
      const nextFlag = "";
      const _config = {
        nextFlag,
        count: 1000,
        ...config,
      };
      return zim.queryRoomMemberList(roomID, _config).then((res) => {
        if (!nextFlag) this.memberList.length = 0;
        this.memberList.push(...res.memberList);
        res.memberList.forEach((item: { userID: string | number }) => {
          this.userMap[item.userID] = { ...item };
        });
        if (res.nextFlag) {
          this.queryRoomMember(roomID, { nextFlag: res.nextFlag });
        }
        this.queryRoomOnlineMemberCount(roomID);
      });
    },

    queryRoomOnlineMemberCount(roomID: string) {
      return zim.queryRoomOnlineMemberCount(roomID).then((res) => {
        this.totalMemberCount = res.count;
      });
    },

    queryAllGroupInfo(id: string) {
      return zim.queryGroupInfo(id).then((res) => {
        this.convInfo.type = 2;
        this.convInfo.conversationID = id;
        this.convInfo.conversationName = res.groupInfo.baseInfo.groupName;
        this.convInfo.notice = res.groupInfo.groupNotice;
        this.convInfo.notificationStatus = res.groupInfo.notificationStatus || 0;
        this.queryGroupMemberList();
        this.queryGroupAttributes(id).then((res: any) => {
          this.convInfo.note = res.groupAttributes.remark;
        });
      });
    },

    getCallList() {
      this.callList.length = 0;
      const config = { count: 100, nextFlag: 0 };
      const cb = (res: any) => {
        this.callList.push(...res.callList);
        if (res.nextFlag) {
          config.nextFlag = res.nextFlag;
          zim.queryCallInvitationList(config).then(cb);
        }
      };
      zim.queryCallInvitationList(config).then(cb);
    },

    callInvite(invitees: string[], mode: number, timeout: number) {
      const config = { mode, timeout: timeout || 90, ...createCallConfig(this.appGlobalConfig, "Invite") };
      return zim.callInvite(invitees, config).then((res) => {
        this.callInfo.callID = res.callID;
        this.callInfo.caller = this.user.userID;
        this.callInfo.mode = mode;
        this.callInfo.state = 1;
        this.callInfo.userStateMap = {};
        this.callInfo.createTime = 0;
        this.callInfo.acceptTime = 0;
        this.callInfo.quitTime = 0;
        this.callInfo.endTime = 0;
        this.callInfo.isShow = false;
        this.callInfo.selfState = -1;
        this.callInfo.isMultiDevice = false;
      });
    },

    callingInvite(invitees: string[]) {
      return zim.callingInvite(invitees, this.callInfo.callID, createCallConfig(this.appGlobalConfig, "Inviting"));
    },

    callAccept() {
      this.callInfo.isShow = false;
      return zim.callAccept(this.callInfo.callID, { extendedData: "Accept" });
    },

    callReject() {
      this.callInfo.isShow = false;
      return zim.callReject(this.callInfo.callID, { extendedData: "Reject" });
    },

    callQuit() {
      return zim.callQuit(this.callInfo.callID, createCallConfig(this.appGlobalConfig, "Quit")).then((res) => {
        this.callInfo.state = 2;
        Object.assign(this.callInfo, res);
      });
    },

    callEnd() {
      return zim.callEnd(this.callInfo.callID, createCallConfig(this.appGlobalConfig, "End")).then((res) => {
        this.callInfo.state = 2;
        Object.assign(this.callInfo, res);
      });
    },

    callCancel(invitees: string[]) {
      return zim
        .callCancel(invitees, this.callInfo.callID, createCallConfig(this.appGlobalConfig, "Caller-Cancel"))
        .then(() => (this.callInfo.state = 2));
    },

    callJoin(callID: string) {
      return zim.callJoin(callID, createCallConfig(this.appGlobalConfig, "Join")).then((res) => {
        this.callInfo.state = 2;
        Object.assign(this.callInfo, res);
      });
    },

    setCallInfo(info: any = {}, isMultiDevice = false) {
      if (info.userStateMap) {
        if (info.callID && info.callID == this.callInfo.callID) {
          info.userStateMap = { ...this.callInfo.userStateMap, ...info.userStateMap };
        }
        this.callInfo.userStateMap = info.userStateMap;
        console.log("callUserMap", JSON.stringify(info.userStateMap));
        const selfState = info.userStateMap[this.user.userID];
        this.callInfo.selfState = selfState;
        if ([1, 2, 3, 6, 7].includes(selfState)) {
          this.callInfo.isShow = false;
          if (this.callInfo.mode != 1 && this.callInfo.caller != this.user.userID) this.callInfo.state = 2;
        }
      }
      delete info.userStateMap;
      if (isMultiDevice) {
        this.callInfo.isShow = false;
        this.callInfo.isMultiDevice = true;
      }
      Object.assign(this.callInfo, info);
    },

    revokeMessage(message: any) {
      return zim.revokeMessage(message, { revokeExtendedData: `✨ revoke mesaage. ✨` }).then((res) => {
        const index = this.msgList.findIndex((item: any) => item.msg.messageID == message.messageID);
        this.msgList.splice(index, 1, { msg: res.message });
      });
    },

    deleteMessage(message: any) {
      return zim
        .deleteMessages([message], message.conversationID, message.conversationType, {
          isAlsoDeleteServerMessage: true,
        })
        .then(() => {
          const index = this.msgList.findIndex((item: any) => item.msg.messageID == message.messageID);
          this.msgList.splice(index, 1);
        });
    },
  },
});

const createCallConfig = (offlinePush: any, extendedData: string) => ({
  extendedData,
  pushConfig: {
    title: extendedData + " web push title",
    content: extendedData + " web push content",
    payload: extendedData + " web push payload",
    resourcesID: offlinePush.resourcesID,
    voIPConfig: offlinePush.voIPConfig,
  },
});

const groupStateTips: any = { 1: "Created", 2: "Dismissed", 3: "Joined", 4: "Invited", 5: "Left", 6: "KickedOut" };
const groupMemberStateTips: any = { 1: "Joined", 2: "Left", 4: "KickedOut", 5: "Invited" };

window.addEventListener("unhandledrejection", (ev: any) => {
  const error = ev.reason || {};
  ElMessage.error(`code: ${error.code}, message: ${error.message}`);
  if (error.code == 6000121 || error.code == 6000111) {
    const zimStore = store();
    zimStore.logout(false);
  }
});

export default store;

// ts-ignore
// const _ZIM = window.ZIM;
ZIM.create(appConfig);
export const zim = ZIM.getInstance() as ZIM;
export const SDKVersion = ZIM.getVersion();

// ts-ignore
window.zim = zim;
// ts-ignore
window.$store = store;
document.title = `ZIM-${SDKVersion}`;
