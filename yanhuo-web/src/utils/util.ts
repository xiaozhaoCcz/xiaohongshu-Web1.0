export const loadImageEnd = (list: any, callback: any, basePath: any) => {
  if (!list || list.length === 0) return;

  if (basePath) list = list.map((item) => basePath + item);
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
