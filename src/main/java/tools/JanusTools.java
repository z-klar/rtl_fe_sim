package tools;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.GlobalData;
import common.RestCallOutput;
import commonEnum.DisplayType;
import dto.ConfigurationData;
import dto.RelayDefinitionDTO;
import dto.TestrackDTO;
import dto.TestrackDisplayDTO;
import dto.janus.JanusSessionsResponseDTO;
import service.RestCallService;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class JanusTools {

    /**
     *
     * @param src
     * @return
     */
    public static JanusSessionsResponseDTO getJanusSessionListResp(String src) {
        JanusSessionsResponseDTO resp;
        try {
            ObjectMapper mapper = new ObjectMapper();
            resp = mapper.readValue(src, JanusSessionsResponseDTO.class);
            return resp;
        }
        catch(Exception ex) {
            resp = new JanusSessionsResponseDTO();
            resp.setTransaction(ex.getMessage());
            return resp;
        }
    }


}
