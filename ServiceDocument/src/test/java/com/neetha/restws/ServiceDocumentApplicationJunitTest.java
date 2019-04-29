package com.neetha.restws;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.neetha.restws.controller.FileController;

@RunWith(SpringRunner.class)
@WebMvcTest(value = FileController.class, secure = false)
public class ServiceDocumentApplicationJunitTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private FileController fileController;

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

	}

	@Test
	public void testDownloadFile() throws Exception {

		mockMvc.perform(get("/storage/documents/IKHFWAOAA63PIPCNMCZG")).andExpect(status().isOk());
	}

	@Test
	// Couldn't get enough time to fix, Returning status 405
	public void testUploadFile() throws Exception {
		MockMultipartFile file = new MockMultipartFile("data", "testFilename.txt", "text/plain",
				"some content".getBytes());

//		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders
//				.fileUpload("/storage/documents/IKHFWAOAA63PIPCNMCZG");
//		builder.with(new RequestPostProcessor() {
//			@Override
//			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
//				request.setMethod("POST");
//				return request;
//			}
//		});
//
//		mockMvc.perform(builder.file("testFilename.txt", "some content".getBytes())).andExpect(status().isOk());

		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/storage/documents/IKHFWAOAA63PIPCNMCZG")
				.file("testFilename.txt", "some content".getBytes())).andExpect(status().isCreated());
	}

	@Test
	// Couldn't get enough time to fix, Returning status 400
	public void putMethod() throws Exception {

		MockMultipartFile file = new MockMultipartFile("file", "newtestFilename.txt", "text/plain",
				"some content".getBytes());

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders
				.fileUpload("/storage/documents/IKHFWAOAA63PIPCNMCZG");
		builder.with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("PUT");
				return request;
			}
		});
		mockMvc.perform(builder.file("testFilename.txt", "some content".getBytes())).andExpect(status().isOk());


	}

	@Test
	public void deleteMethod() throws Exception {
		mockMvc.perform(delete("/storage/documents/IKHFWAOAA63PIPCNMCZG")).andExpect(status().isOk()).andReturn();

	}

}
