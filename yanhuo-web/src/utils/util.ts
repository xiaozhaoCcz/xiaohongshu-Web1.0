
export const formatDate = (t: number): string => {
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
export const formateTime = (time: number): string => {
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
    return "刚刚";
  }
};

/**
 * 随机生成字符串
 * @param len 指定生成字符串长度
 */
export const getRandomString = (len: number) => {
  const _charStr = "abacdefghjklmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ0123456789",
    min = 0,
    max = _charStr.length - 1;
  let _str = ""; //定义随机字符串 变量
  //判断是否指定长度，否则默认长度为15
  len = len || 15;
  //循环生成字符串
  for (let i = 0, index; i < len; i++) {
    index = (function (randomIndexFunc, i) {
      return randomIndexFunc(min, max, i, randomIndexFunc);
    })(function (min: any, max: any, i: any, _self: any) {
      let indexTemp = Math.floor(Math.random() * (max - min + 1) + min);
      const numStart = _charStr.length - 10;
      if (i == 0 && indexTemp >= numStart) {
        indexTemp = _self(min, max, i, _self);
      }
      return indexTemp;
    }, i);
    _str += _charStr[index];
  }
  return _str;
}
