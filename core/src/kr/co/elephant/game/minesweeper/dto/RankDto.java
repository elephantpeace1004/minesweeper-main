package kr.co.elephant.game.minesweeper.dto;

import java.util.List;

public class RankDto {
    List<Content> content;

    public List<Content> getContent() {
        return content;
    }
    public void setContent(List<Content> content) {
        this.content = content;
    }

     public static class Content {
        String memberId;
        String memberTime;
        String memberLocation;
        String updateDate;

        String memberLevel;
        String memberScore;

         public String getMemberLevel() {
             return memberLevel;
         }

         public void setMemberLevel(String memberLevel) {
             this.memberLevel = memberLevel;
         }

         public String getMemberScore() {
             return memberScore;
         }

         public void setMemberScore(String memberScore) {
             this.memberScore = memberScore;
         }

         public String getMemberId() {
             return memberId;
         }

         public void setMemberId(String memberId) {
             this.memberId = memberId;
         }

         public String getMemberTime() {
             return memberTime;
         }

         public void setMemberTime(String memberTime) {
             this.memberTime = memberTime;
         }

         public String getMemberLocation() {
             return memberLocation;
         }

         public void setMemberLocation(String memberLocation) {
             this.memberLocation = memberLocation;
         }

         public String getUpdateDate() {
             return updateDate;
         }

         public void setUpdateDate(String updateDate) {
             this.updateDate = updateDate;
         }
     }
}
