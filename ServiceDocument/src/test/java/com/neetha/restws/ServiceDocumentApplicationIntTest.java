package com.neetha.restws;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ServiceDocumentApplicationIntTest {
	protected MockMvc mockMvc;

	@Test
	public void testDownloadFile() throws Exception {
		mockMvc.perform(get("/storage/documents/IKHFWAOAA63PIPCNMCZG"));
	}

	@Test
	public void postMethod() throws Exception {
		//mockMvc.perform(post("/storage/documents")).andExpect(status().isCreated());
	}

	@Test
	public void putMethod() throws Exception {
		//mockMvc.perform(put("/storage/documents/IKHFWAOAA63PIPCNMCZG")).andExpect(status().isNoContent()).andReturn();
	}

	@Test
	public void deleteMethod() throws Exception {
		//mockMvc.perform(delete("/storage/documents/IKHFWAOAA63PIPCNMCZG")).andExpect(status().isNoContent())
			//	.andReturn();
	}

}
