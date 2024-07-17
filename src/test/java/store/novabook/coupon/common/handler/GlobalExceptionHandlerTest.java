package store.novabook.coupon.common.handler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
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

@WithMockUser
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = TestController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
			.setControllerAdvice(new GlobalExceptionHandler())
			.build();
	}

	@Test
	void testHandleMethodArgumentNotValid() throws Exception {
		Map<String, Object> invalidRequest = new HashMap<>();
		invalidRequest.put("name", "");

		mockMvc.perform(post("/test/valid").contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(invalidRequest))).andExpect(status().isBadRequest());
	}

	@Test
	void testHandleNotFoundException() throws Exception {
		mockMvc.perform(get("/test/not-found")).andExpect(status().isNotFound());
	}

	@Test
	void testHandleForbiddenException() throws Exception {
		mockMvc.perform(get("/test/forbidden")).andExpect(status().isForbidden());
	}

	@Test
	void testHandleNovaException() throws Exception {
		mockMvc.perform(get("/test/nova-exception")).andExpect(status().isBadRequest());
	}

	@Test
	void testHandleException() throws Exception {
		mockMvc.perform(get("/test/exception")).andExpect(status().isInternalServerError());
	}

	@Test
	void testHandleTypeMismatch() throws Exception {
		mockMvc.perform(get("/test/type-mismatch").param("value", "invalid")).andExpect(status().isBadRequest());
	}
}
