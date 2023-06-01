function getEmoji() {
  let thumbnail = document.querySelectorAll(".thumbnail");
  let emojiList = [];
  let i = 0;
  for (let t of thumbnail) {
    if(i == 95){
      break;
    }
    let img = t.querySelector(".lazy");
    let imgSrc = img.getAttribute("src");
    let emojiName = t.getAttribute("data-original-title").replace("emoji(点击即复制)", "");
    let emoji = {
      src: imgSrc,
      name: emojiName,
      regName: `[${emojiName}]`
    }
    emojiList.push(emoji);
    i++;
  }

  return emojiList;
}
console.log(JSON.stringify(getEmoji()))

