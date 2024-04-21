package kr.co.elephant.game.minesweeper.dto;

public class LoginDto {

    private String memberId;
    private String password;
    private int agentSeq;
    private String memberApi;
    private String deviceId;
    private String osCode;

    public LoginDto(String memberId, String password,  int agentSeq, String memberApi, String deviceId, String osCode) {
        this.memberId = memberId;
        this.password = password;
        this.agentSeq = agentSeq;
        this.memberApi = memberApi;
        this.deviceId = deviceId;
        this.osCode = osCode;
    }
}
