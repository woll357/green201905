package db_p;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BoardDTO {
   
   String title, pname, pw, content, upfile;
   Date regdateStr;
   
   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getPname() {
      return pname;
   }

   public void setPname(String pname) {
      this.pname = pname;
   }

   public String getPw() {
      return pw;
   }

   public void setPw(String pw) {
      this.pw = pw;
   }

   public String getContent() {
      return content;
   }
   
   public String getContentBr() {
	      return content.replaceAll("\r\n", "<br>");      //내용 쓴거 밑에줄로 넘겨주는거.
	
   }

   public void setContent(String content) {
      this.content = content;
   }

   public String getUpfile() {
      return upfile;
   }

   public void setUpfile(String upfile) {
      this.upfile = upfile;
   }

   public String getRegdateStr() {
      return sdf.format(regdate);
   }

   public void setRegdateStr(String regdateStr) {
      try {
         this.regdateStr =sdf.parse(regdateStr);
      } catch (ParseException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public Integer getBid() {
      return bid;
   }

   public void setBid(Integer bid) {
      this.bid = bid;
   }

   public Integer getGid() {
      return gid;
   }

   public void setGid(Integer gid) {
      this.gid = gid;
   }

   public Integer getNo() {
      return no;
   }

   public void setNo(Integer no) {
      this.no = no;
   }

   public Integer getSeq() {
      return seq;
   }

   public void setSeq(Integer seq) {
      this.seq = seq;
   }

   public Integer getLevel() {
      return level;
   }

   public void setLevel(Integer level) {
      this.level = level;
   }

   public Date getRegdate() {
      return regdate;
   }

   public void setRegdate(Date regdate) {
      this.regdate = regdate;
   }

   Integer bid, gid, no, seq, level;
   Date regdate;
   
   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   

}