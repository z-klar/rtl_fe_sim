package dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenDto {
    @JsonProperty("access_token")
    protected String token;
    @JsonProperty("refresh_token")
    protected String refreshToken;
    @JsonProperty("expires_in")
    protected long expiresIn;
    @JsonProperty("refresh_expires_in")
    protected long refreshExpiresIn;
    @JsonProperty("token_type")
    protected String tokenType;


    @Override
    public String toString() {
        return "UserToken{" +
                "access_token=" + token +
                "refresh_token=" + refreshToken +
                "expires_in=" + expiresIn +
                "refresh_expires_in=" + refreshExpiresIn +
                "token_type=" + tokenType +
                "}";
    }
}
