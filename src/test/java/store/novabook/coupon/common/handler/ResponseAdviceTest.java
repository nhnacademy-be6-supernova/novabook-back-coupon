package store.novabook.coupon.common.handler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.coupon.common.exception.ErrorCode;

@WithMockUser
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = TestController.class)
@Import({ResponseAdvice.class, GlobalExceptionHandler.class})
class ResponseAdviceTest {

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
			.setControllerAdvice(new ResponseAdvice(), new GlobalExceptionHandler())
			.build();
	}

	@Test
	void testHandleNotFoundException() throws Exception {
		mockMvc.perform(get("/test/not-found"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.header.isSuccessful").value(false))
			.andExpect(jsonPath("$.body.errorCode").value(ErrorCode.COUPON_NOT_FOUND.name()))
			.andExpect(jsonPath("$.body.message").value("해당 쿠폰이 존재하지 않습니다."));
	}

	@Test
	void testHandleForbiddenException() throws Exception {
		mockMvc.perform(get("/test/forbidden"))
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$.header.isSuccessful").value(false))
			.andExpect(jsonPath("$.body.errorCode").value(ErrorCode.FORBIDDEN.name()))
			.andExpect(jsonPath("$.body.message").value("접근 권한이 없습니다."));
	}

	@Test
	void testHandleNovaException() throws Exception {
		mockMvc.perform(get("/test/nova-exception"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.header.isSuccessful").value(false))
			.andExpect(jsonPath("$.body.errorCode").value(ErrorCode.INVALID_REQUEST_ARGUMENT.name()))
			.andExpect(jsonPath("$.body.message").value("잘못된 요청입니다."));
	}

	@Test
	void testHandleException() throws Exception {
		mockMvc.perform(get("/test/exception"))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.header.isSuccessful").value(false))
			.andExpect(jsonPath("$.body.errorCode").value(ErrorCode.INTERNAL_SERVER_ERROR.name()))
			.andExpect(jsonPath("$.body.message").value("서버 내부에 문제가 발생했습니다."));
	}

	@Test
	void testHandleTypeMismatch() throws Exception {
		mockMvc.perform(get("/test/type-mismatch").param("value", "invalid"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.header.isSuccessful").value(false))
			.andExpect(jsonPath("$.body.errorCode").value(ErrorCode.INVALID_ARGUMENT_TYPE.name()))
			.andExpect(jsonPath("$.body.message").value("유효하지 않은 인자입니다."));
	}

	@Test
	void testValidErrorResponse() throws Exception {
		Map<String, Object> invalidRequest = new HashMap<>();
		invalidRequest.put("name", ""); // Invalid name field

		mockMvc.perform(post("/test/valid").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(invalidRequest)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.header.isSuccessful").value(false))
			.andExpect(jsonPath("$.body").exists());
	}
}

