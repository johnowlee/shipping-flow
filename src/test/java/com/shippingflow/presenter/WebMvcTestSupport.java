package com.shippingflow.presenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shippingflow.presenter.api.item.ItemController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        ItemController.class
})
public abstract class WebMvcTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
