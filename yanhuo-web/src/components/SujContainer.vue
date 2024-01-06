<template>
  <div class="sug-container-wrapper query-trending sug-pad">
    <div class="sug-container query-trending">
      <div class="history">
        <div class="header">
          <span> 历史记录 </span>
          <div class="icon-group">
            <div class="icon-box">
              <svg data-v-7c2d5134="" class="reds-icon icon" width="16" height="16">
                <use data-v-7c2d5134="" xlink:href="#delete"></use></svg
              ><!---->
            </div>
            <!---->
          </div>
        </div>
        <div class="history-list">
          <div class="history-item">
            斗破苍穹漫画
            <!---->
          </div>
          <div class="history-item">
            小红书网页版
            <!---->
          </div>
          <!---->
        </div>
      </div>
      <div class="sug-box">
        <div class="header">猜你想搜</div>
        <div class="sug-wrapper">
          <div class="sug-item query-trending query-trending hotspot">
            <div class="sug-text">
              斗破苍穹漫画
              <!---->
            </div>
            <!---->
          </div>
          <div class="sug-item query-trending query-trending hotspot">
            <div class="sug-text">
              坂田银时
              <!---->
            </div>
            <!---->
          </div>
          <div class="sug-item query-trending query-trending hotspot">
            <div class="sug-text">
              胖男生穿搭
              <!---->
            </div>
            <!---->
          </div>
          <div class="sug-item query-trending query-trending hotspot">
            <div class="sug-text">
              朱竹清稀有图片
              <!---->
            </div>
            <!---->
          </div>
          <div class="sug-item query-trending query-trending hotspot">
            <div class="sug-text">
              美杜莎全身
              <!---->
            </div>
            <!---->
          </div>
          <div class="sug-item query-trending query-trending hotspot">
            <div class="sug-text">
              男士冬季外套
              <!---->
            </div>
            <!---->
          </div>
        </div>
      </div>
      <div>
        <div class="hotspots">
          <div class="header">
            <img
              class="header-img"
              src="https://picasso-static.xiaohongshu.com/fe-platform/0c824dd79820e11ad8e3a5c86d56892d6159f0a8.png"
              crossorigin="anonymous"
            />
          </div>
          <div class="hotspot-list">
            <div class="hotspot-item">
              <p class="hotspot-index">1</p>
              <div class="hotspot-title">
                <span class="text">收到了昆凌从北欧寄的明信片</span>
                <img
                  src="https://sns-img-qc.xhscdn.com/search/trends/icon/label/new/version/1"
                  crossorigin="anonymous"
                />
              </div>
              <span class="hotspot-score">1100.3w</span>
            </div>
            <div class="hotspot-item">
              <p class="hotspot-index">2</p>
              <div class="hotspot-title">
                <span class="text">纸和光影为我招来满屋蝴蝶</span>
                <img
                  src="https://sns-img-qc.xhscdn.com/search/trends/icon/label/hot/version/1"
                  crossorigin="anonymous"
                />
              </div>
              <span class="hotspot-score">987.6w</span>
            </div>
            <div class="hotspot-item">
              <p class="hotspot-index">3</p>
              <div class="hotspot-title">
                <span class="text">拍下了属于树的春夏秋冬</span>
                <!---->
              </div>
              <span class="hotspot-score">985.6w</span>
            </div>
            <div class="hotspot-item">
              <p class="hotspot-index">4</p>
              <div class="hotspot-title">
                <span class="text">假如我在博物馆看上某样东西</span>
                <!---->
              </div>
              <span class="hotspot-score">985.9w</span>
            </div>
            <div class="hotspot-item">
              <p class="hotspot-index">5</p>
              <div class="hotspot-title">
                <span class="text">跟着87岁男孩学变帅公式</span>
                <!---->
              </div>
              <span class="hotspot-score">972.4w</span>
            </div>
            <div class="hotspot-item">
              <p class="hotspot-index">6</p>
              <div class="hotspot-title">
                <span class="text">跟王家卫学会拍繁花感复古照</span>
                <img
                  src="https://sns-img-qc.xhscdn.com/search/trends/icon/label/hot/version/1"
                  crossorigin="anonymous"
                />
              </div>
              <span class="hotspot-score">965.1w</span>
            </div>
            <div class="hotspot-item">
              <p class="hotspot-index">7</p>
              <div class="hotspot-title">
                <span class="text">新年新流行：轨迹画龙</span>
                <!---->
              </div>
              <span class="hotspot-score">925.1w</span>
            </div>
            <div class="hotspot-item">
              <p class="hotspot-index">8</p>
              <div class="hotspot-title">
                <span class="text">骨相立体跟港风妆容真的很搭</span>
                <!---->
              </div>
              <span class="hotspot-score">874.1w</span>
            </div>
            <div class="hotspot-item">
              <p class="hotspot-index">9</p>
              <div class="hotspot-title">
                <span class="text">冬天去不了武功山不如去浪漫广西</span>
                <!---->
              </div>
              <span class="hotspot-score">853.4w</span>
            </div>
            <div class="hotspot-item">
              <p class="hotspot-index">10</p>
              <div class="hotspot-title">
                <span class="text">当美食遇上非遗：自制福建线面</span>
                <!---->
              </div>
              <span class="hotspot-score">722.8w</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup></script>
<style lang="less" scoped>
//隐藏滚动条
.sug-container-wrapper::-webkit-scrollbar {
  display: none;
}
.sug-container-wrapper.query-trending {
  position: relative;
  padding-top: 100%;
}
.sug-container-wrapper {
  margin-top: 8px;
  width: 100%;
  background-color: #fff;
  box-shadow: 0 4px 32px 0 rgba(0, 0, 0, 0.08), 0 1px 4px 0 rgba(0, 0, 0, 0.04);
  border-radius: 12px;
  overflow: scroll;
}

.sug-container.query-trending {
  width: 100%;
  position: absolute;
  top: 0;

  .history {
    padding: 4px;

    .header {
      display: flex;
      padding: 0 4px 0 12px;
      align-items: center;
      height: 32px;
      font-style: normal;
      font-weight: 400;
      font-size: 12px;
      line-height: 120%;
      color: rgba(51, 51, 51, 0.6);

      .icon-group {
        display: flex;
        margin-left: auto;
        font-size: 12px;
        font-weight: 400;
        color: rgba(51, 51, 51, 0.8);
        grid-gap: 4px;
        gap: 4px;
        .icon-box {
          display: flex;
          align-items: center;
          justify-content: center;
          height: 24px;
          width: 24px;
          grid-gap: 4px;
          gap: 4px;
          padding: 0 4px;
          cursor: pointer;
        }
      }
    }

    .history-list {
      display: flex;
      align-items: center;
      padding: 0 8px 8px;
      flex-wrap: wrap;
      position: relative;
      grid-gap: 8px;
      gap: 8px;

      .history-item {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 32px;
        color: rgba(51, 51, 51, 0.8);
        font-size: 14px;
        font-weight: 400;
        line-height: 120%;
        padding: 0 12px;
        white-space: nowrap;
        background: rgba(0, 0, 0, 0.03);
        border-radius: 999px;
        border: 1px solid transparent;
        cursor: pointer;
      }
    }
  }

  .sug-box {
    padding: 4px;

    .header {
      display: flex;
      padding: 10.5px 12px;
      align-items: center;
      height: 32px;
      font-style: normal;
      font-weight: 400;
      font-size: 12px;
      line-height: 120%;
      color: rgba(51, 51, 51, 0.6);
    }

    .sug-wrapper {
      display: flex;
      flex-wrap: wrap;

      .query-trending.hotspot:nth-child(odd) {
        margin-right: 2px;
      }

      .query-trending.hotspot {
        width: calc(50% - 2px);
      }

      .query-trending {
        color: rgba(51, 51, 51, 0.8);
        width: 100%;
      }

      .sug-item {
        width: 100%;
        padding: 0 12px;
        font-size: 16px;
        height: 40px;
        line-height: 120%;
        font-weight: 400;
        border-radius: 8px;
        color: rgba(51, 51, 51, 0.6);
        display: flex;
        align-items: center;
      }

      .sug-text {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 20px;
      }
    }
  }

  .hotspots {
    padding: 4px;

    .header {
      padding: 8px 12px;
      height: 32px;
    }

    .hotspot-item:first-child {
      color: #ff2442;
    }
    .hotspot-item {
      padding: 0 12px;
      display: flex;
      align-items: center;
      height: 40px;
      cursor: pointer;
      color: rgba(51, 51, 51, 0.6);

      .hotspot-index {
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: 500;
        font-size: 14px;
        line-height: 120%;
        width: 16px;
        height: 16px;
      }

      .hotspot-title {
        margin: 0 6px;
        color: rgba(51, 51, 51, 0.8);
        font-weight: 400;
        font-size: 16px;
        line-height: 120%;
        display: flex;
        align-items: center;
        height: 100%;
        flex: 1;

        img {
          margin-left: 6px;
          height: 14px;
        }

        .text {
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }

      .hotspot-score {
        color: rgba(51, 51, 51, 0.3);
        margin-left: auto;
        font-weight: 400;
        font-size: 12px;
        line-height: 120%;
      }
    }
  }
}

.sug-container.query-trending {
  width: 100%;
  position: absolute;
  top: 0;
}
</style>
