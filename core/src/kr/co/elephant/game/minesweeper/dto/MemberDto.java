package kr.co.elephant.game.minesweeper.dto;

public class MemberDto {

    private String memberId;
    private String password;
    private String name;
    private String email;
    private int agentSeq;
    private String memberLocation;
    private String memberApi;
    private String deviceId;
    private String osCode;

    public MemberDto(String memberId, String password,  int agentSeq, String memberApi, String deviceId, String osCode) {
        this.memberId = memberId;
        this.password = password;
        this.agentSeq = agentSeq;
        this.memberApi = memberApi;
        this.deviceId = deviceId;
        this.osCode = osCode;
    }

    public MemberDto(String memberId, String password, String name, String email, int agentSeq, String memberLocation, String memberApi, String deviceId, String osCode) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.agentSeq = agentSeq;
        this.memberLocation = memberLocation;
        this.memberApi = memberApi;
        this.deviceId = deviceId;
        this.osCode = osCode;
    }


}
