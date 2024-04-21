package kr.co.elephant.game.minesweeper.network;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kr.co.elephant.game.minesweeper.common.SettingConfig;
import kr.co.elephant.game.minesweeper.dto.LoginDto;
import kr.co.elephant.game.minesweeper.dto.MemberDataDto;
import kr.co.elephant.game.minesweeper.dto.MemberDto;
import kr.co.elephant.game.minesweeper.dto.RankDto;
import kr.co.elephant.game.minesweeper.dto.ResponseDto;

public class HttpApi {

    private static final String DOMAIN = "http://152.67.201.25:17000/";
    private static final String REGISTER_URL = DOMAIN + "register";
    private static final String LOGIN_URL = DOMAIN + "login";
    private static final String MEMBER_DATA_URL = DOMAIN + "member/data";
    public static final String MEMBER_RANK_URL = DOMAIN + "member/data/top/level/score";

    public static void getRank() {
        HttpRequestManager.sendHttpGetRequest(MEMBER_RANK_URL, new HttpRequestManager.HttpRequestListener() {
            @Override
            public void onSuccess(ResponseDto responseDto) {
                // 요청이 성공적으로 전송되었을 때 처리할 작업
                Gdx.app.log("sendRegisterRequest", "getRank succ" + responseDto.getData());

                Gson gson = new Gson();
                String jsonString = gson.toJson(responseDto.getData());
                RankDto rankDto = gson.fromJson(jsonString, RankDto.class);
            }
            @Override
            public void onFailure(int statusCode, String responseBody) {
                // 요청이 실패했을 때 처리할 작업
                Gdx.app.log("sendRegisterRequest", "getRank fail" + responseBody);
            }
        });
    }

    public static void postMemberData(String memberId, String memberLevel, String memberTime) {
        // 현재 기기의 국적(로케일) 가져오기
        Locale currentLocale = Locale.getDefault();
        String countryCode = currentLocale.getCountry();
        MemberDataDto memberDataDto = new MemberDataDto(
                memberId,               // memberId
                memberLevel,            // memberLevel
                "",                     // memberScore
                memberTime,             // memberTime
                1,                      // agentSeq
                countryCode,            // memberLocation
                "android"               // memberApi
        );
        Gson gson = new Gson();
        String requestBody = gson.toJson(memberDataDto);
        Gdx.app.log("sendRegisterRequest", "requestBody " + requestBody);
        HttpRequestManager.sendHttpPostRequest(MEMBER_DATA_URL, requestBody, new HttpRequestManager.HttpRequestListener() {
            @Override
            public void onSuccess(ResponseDto responseDto) {
                // 요청이 성공적으로 전송되었을 때 처리할 작업
                Gdx.app.log("sendRegisterRequest", "memberDataDto succ" + responseDto.getData());
            }
            @Override
            public void onFailure(int statusCode, String responseBody) {
                // 요청이 실패했을 때 처리할 작업
                Gdx.app.log("sendRegisterRequest", "memberDataDto fail" + responseBody);
            }
        });
    }



    public static void postLogin(String memberId) {

        LoginDto loginDto = new LoginDto(
                memberId,                     // memberId
                "qwer",                     // password
                1,                          // agentSeq
                "login",                   // memberApi
                memberId,                     // deviceId
                "android"                   // osCode
        );
        Gson gson = new Gson();
        String requestBody = gson.toJson(loginDto);
        HttpRequestManager.sendHttpPostRequest(LOGIN_URL, requestBody, new HttpRequestManager.HttpRequestListener() {
            @Override
            public void onSuccess(ResponseDto responseDto) {
                // 요청이 성공적으로 전송되었을 때 처리할 작업
                Gdx.app.log("sendRegisterRequest", "postLogin succ" + responseDto.getData());
            }
            @Override
            public void onFailure(int statusCode, String responseBody) {
                // 요청이 실패했을 때 처리할 작업
                Gdx.app.log("sendRegisterRequest", "postLogin fail" + responseBody);
            }
        });
    }

    public static void postMemberRegister() {

        try {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String formattedNowDate = dateFormat.format(now);
            int randomPIN4 = (int) (Math.random() * 9000) + 1000;
            String idTail = String.valueOf(randomPIN4);
            final String autoId = "ID" + formattedNowDate + idTail;
            Gdx.app.log("sendRegisterRequest", "autoId " + autoId);
            MemberDto memberDto = new MemberDto(
                    autoId,                     // memberId
                    "qwer",                     // password
                    autoId,                     // name
                    autoId+"@elephant.com",    // email
                    1,                          // agentSeq
                    "korea",                    // memberLocation
                    "regist",                   // memberApi
                    autoId,                     // deviceId
                    "android"                   // osCode
            );

            Gson gson = new Gson();
            String requestBody = gson.toJson(memberDto);
            Gdx.app.log("sendRegisterRequest", "requestBody " + requestBody);

            HttpRequestManager.sendHttpPostRequest(REGISTER_URL, requestBody, new HttpRequestManager.HttpRequestListener() {
                @Override
                public void onSuccess(ResponseDto responseDto) {
                    // 요청이 성공적으로 전송되었을 때 처리할 작업
                    Gdx.app.log("sendRegisterRequest", "postMemberRegister succ" + responseDto.getData());
                    SettingConfig.setMemberId(autoId);
                }
                @Override
                public void onFailure(int statusCode, String responseBody) {
                    // 요청이 실패했을 때 처리할 작업
                    Gdx.app.log("sendRegisterRequest", "postMemberRegister fail" + responseBody);
                }
            });


        }catch (Exception e){
            Gdx.app.log("sendRegisterRequest", "Exception " + e.getMessage());
        }
    }


}
