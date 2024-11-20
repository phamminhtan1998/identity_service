package minhtan.authenticator.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.jbosslog.JBossLog;
import minhtan.authenticator.model.OTPReq;
import minhtan.authenticator.model.OtpResp;
import minhtan.authenticator.model.QRResp;
import okhttp3.*;


import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@JBossLog
public class SmsProvider {

    private final OkHttpClient httpClient;
    private final ExecutorService executorService;

    private final ObjectMapper objectMapper;

    public static final String BASE_URL = "http://localhost:9003/";
    public static final String GEN_QR_LINK = "generate-qr";
    public static final String VALID_OTP_LINK = "verify-otp";
    public SmsProvider() {
        this.httpClient = new OkHttpClient();
        this.executorService = Executors.newFixedThreadPool(5);
        this.objectMapper = new ObjectMapper();
    }

    public String generateQRCode(String username) {
        String url = buildUrl(GEN_QR_LINK);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("username", username);
        Request request = new Request.Builder().url(urlBuilder.build().toString())
                .build();
        try {
            okhttp3.Response response = httpClient.newCall(request).execute();
            QRResp qrResp = objectMapper.readValue(response.body().bytes(), QRResp.class);
            log.infof("String value :%s", qrResp.toString());
            return qrResp.getQrImg();
        } catch (IOException e) {
            return "";
        }
    }

    public boolean validOtp(String username, int otp)  {
        String url = buildUrl(VALID_OTP_LINK);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        try {
        byte[] requestData = objectMapper.writeValueAsBytes(OTPReq.builder().otp(otp).username(username).build());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData);
        Request request = new Request.Builder().url(urlBuilder.build().toString())
                .post(requestBody)
                .build();
            Response response = httpClient.newCall(request).execute();
            OtpResp otpResp = objectMapper.readValue(response.body().bytes(), OtpResp.class);
            return otpResp.isValidOtp();
        } catch (IOException e) {
            return false;
        }

    }

    public String buildUrl(String url) {
        String concat = BASE_URL.concat(url);
        return concat;
    }
}
