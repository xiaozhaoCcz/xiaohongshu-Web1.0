export const loadImageEnd = (list: any, callback: any, basePath: any) => {
  if (!list || list.length === 0) return;

  if (basePath) list = list.map((item: any) => basePath + item);
  const img = new Image();
  img.data = {
    list: list,
    callback: callback,
    resultList: [],
    num: 0,
  };
  img.addEventListener("load", loadImgHandler);
  img.addEventListener("error", loadImgHandler);
  img.src = list[img.data.num];
  console.log("---img", img.height);
};

const loadImgHandler = (e) => {
  console.log("img", e);
  let data = e.currentTarget.data;
  if (e.type !== "error") {
    data.resultList.push(e.currentTarget.cloneNode(false));
  }
  data.num++;
  if (data.num > data.list.length - 1) {
    e.currentTarget.removeEventListener("load", loadImgHandler);
    if (data.callback) {
      data.callback(data.resultList);
    }
    data = null;
    return;
  }
  e.currentTarget.src = data.list[data.num];
};

export const formatDate = (t: number) => {
  t = t || Date.now();
  const time = new Date(t);
  let str = time.getFullYear();
  str += "-";
  str = time.getMonth() < 9 ? "0" + (time.getMonth() + 1) : time.getMonth() + 1;
  str += "-";
  str += time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
  str += " ";
  str += time.getHours();
  str += ":";
  str += time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
  return str;
};

/**
 * 距当前时间点的时长
 * @prama time 13位时间戳
 * @return str x秒 / x分钟 / x小时
 */
export const formateTime = (time: number) => {
  const second = 1000;
  const minute = second * 60;
  const hour = minute * 60;
  const day = hour * 24;
  const month = day * 30;
  const now = new Date().getTime();
  const diffValue = now - time;

  // 计算差异时间的量级
  const secondC = diffValue / second;
  const minC = diffValue / minute;
  const hourC = diffValue / hour;
  const dayC = diffValue / day;
  const monthC = diffValue / month;

  if (monthC > 3) {
    return formatDate(time);
  } else if (monthC >= 1 && monthC <= 3) {
    return parseInt(monthC.toString()) + "月前";
  } else if (dayC >= 1) {
    return parseInt(dayC.toString()) + "天前";
  } else if (hourC >= 1) {
    return parseInt(hourC.toString()) + "小时前";
  } else if (minC >= 1) {
    return parseInt(minC.toString()) + "分钟前";
  } else if (secondC >= 1) {
    return parseInt(secondC.toString()) + "秒前";
  } else {
    return "0秒";
  }
};
