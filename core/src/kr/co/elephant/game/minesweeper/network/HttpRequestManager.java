package kr.co.elephant.game.minesweeper.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kr.co.elephant.game.minesweeper.dto.ResponseDto;

public class HttpRequestManager {

    public static void sendHttpPostRequest(String url, String requestBody, final HttpRequestListener listener) {
        sendHttpRequest(url, Net.HttpMethods.POST, requestBody, listener);
    }

    public static void sendHttpGetRequest(String url, final HttpRequestListener listener) {
        sendHttpRequest(url, Net.HttpMethods.GET, null, listener);
    }

    private static void sendHttpRequest(String url, String method, String requestBody, final HttpRequestListener listener) {
        try {
            HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
            Net.HttpRequest request = requestBuilder
                    .newRequest()
                    .method(method)
                    .url(url)
                    .header("Content-Type", "application/json") // JSON 형식으로 설정
                    .content(requestBody)
                    .build();

            Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    int statusCode = httpResponse.getStatus().getStatusCode();
                    String responseBody = httpResponse.getResultAsString();
                    Gson gson = new GsonBuilder()
                            .setLenient() // 누락된 속성이 있어도 무시
                            .create();
                    ResponseDto responseDto = gson.fromJson(responseBody, ResponseDto.class);
                    if (statusCode == 200) {
                        listener.onSuccess(responseDto);
                    } else {
                        listener.onFailure(statusCode, responseBody);
                    }
                }

                @Override
                public void failed(Throwable t) {
                    Gdx.app.log("sendRegisterRequest", "failed " + t.getMessage());
                    //listener.onError(t);
                }

                @Override
                public void cancelled() {
                    Gdx.app.log("sendRegisterRequest", "cancelled");
                    //listener.onCancel();
                }
            });
        } catch (Exception e) {
            //listener.onError(e);
        }
    }

    public interface HttpRequestListener {
        void onSuccess(ResponseDto responseDto);
        void onFailure(int statusCode, String responseBody);
//        void onError(Throwable t);
//        void onCancel();
    }


    public static String getResponseJson(ResponseDto response, boolean object){
        Object data = response.getData();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        if(object){
            JsonObject obj = parser.parse(gson.toJson( data )).getAsJsonObject();
            return obj.toString();
        }else{
            JsonArray jsonArray = parser.parse(gson.toJson( data )).getAsJsonArray();
            return jsonArray.toString();
        }
    }
}
