package com.scout24.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HtmlAnalyzerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * End point testing
     *
     * @throws Exception
     */
    @Test
    public void postUrlToAnalyzer() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/analyzer")
                .content("https://jsoup.org/cookbook/input/load-document-from-file")
        ).andExpect(status().isOk()).andReturn();
    }
}
