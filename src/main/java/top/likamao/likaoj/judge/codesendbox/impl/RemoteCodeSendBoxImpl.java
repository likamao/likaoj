package top.likamao.likaoj.judge.codesendbox.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;
import top.likamao.likaoj.common.CodeSendBoxConfigCommon;
import top.likamao.likaoj.common.ErrorCode;
import top.likamao.likaoj.exception.BusinessException;
import top.likamao.likaoj.judge.codesendbox.CodeSendBoxInterface;
import top.likamao.likaoj.judge.codesendbox.model.ExecuteCodeRequest;
import top.likamao.likaoj.judge.codesendbox.model.ExecuteCodeResponse;

/**
 * 远程代码sendbox实现类
 */
@Service
public class RemoteCodeSendBoxImpl implements CodeSendBoxInterface {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        String host = CodeSendBoxConfigCommon.url + CodeSendBoxConfigCommon.api;
        String bodySer = JSONUtil.toJsonStr(executeCodeRequest);
        String response;
        try (HttpResponse httpResponse = HttpUtil.createPost(host)
                .header("Authorization", CodeSendBoxConfigCommon.auth)
                .body(bodySer)
                .execute()) {
            response = httpResponse.body();
        }
        if (response.isBlank()) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remote SendBox error");
        }

        return JSONUtil.toBean(response, ExecuteCodeResponse.class);
    }
}
