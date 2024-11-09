var countdown = 0;
function timesCount() {

  if(countdown >= 0){
    self.postMessage(countdown);
    countdown = countdown - 1;
    // countdown--;
    setTimeout(function(){
      timesCount();
    }, 1000);
  }else{
    self.postMessage(-1);
  }
}

self.addEventListener("message", function(e){
  // e.data 可接收到傳過來的資料
  console.log("收到主程式來的資料：" + e.data);
  countdown = e.data;
  timesCount();
});
