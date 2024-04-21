package kr.co.elephant.game.minesweeper.dto;

public class MemberDataDto {

    private String memberId;
    private String memberLevel;
    private String memberScore;
    private String memberTime;
    private int agentSeq;
    private String memberLocation;
    private String memberApi;

    public MemberDataDto(String memberId, String memberLevel, String memberScore, String memberTime, int agentSeq, String memberLocation, String memberApi) {
        this.memberId = memberId;
        this.memberLevel = memberLevel;
        this.memberScore = memberScore;
        this.memberTime = memberTime;
        this.agentSeq = agentSeq;
        this.memberLocation = memberLocation;
        this.memberApi = memberApi;
    }
}
