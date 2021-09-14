package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
public class CustomerControllerIT extends BaseIT{

    /** @ParameterizedTest(name="#{index} with [{arguments}]")
    @MethodSource("package guru.sfg.brewery.web.controllers.BeerControllerIT%getStreamAdminCustomer")
    void testListCustomersAuth(String user, String pwd) throws Exception{
        mockMvc.perform(get("/customers").with(httpBasic(user,pwd)))
                .andExpect(status().isOk());
    }*/

    @Test
    void testListCustomersNOTAuth() throws Exception{
        mockMvc.perform(get("/customers").with(httpBasic("user","pwd")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListCustomersAuthRoleAdmin() throws Exception{
        mockMvc.perform(get("/customers").with(httpBasic("crinnger","oliveira")))
                .andExpect(status().isOk());
    }

    @Test
    void testListCustomersAuthRoleUser() throws Exception{
        mockMvc.perform(get("/customers").with(httpBasic("samuel","oliveira")))
                .andExpect(status().isForbidden());
    }


    @Test
    void testNewCostumerWithRoleNoAuth() throws Exception{
        mockMvc.perform(post("/customers/new")
                .param("customerName","teste customer")
                .with(httpBasic("samuel","oliveira")))
                .andExpect(status().isForbidden());
    }

    @Test
    void testNewCostumerWithRoleAuth() throws Exception{
        mockMvc.perform(post("/customers/new")
                .param("customerName","teste customer")
                .with(httpBasic("crinnger","oliveira")))
                .andExpect(status().isForbidden());
    }

    @Test
    void testNewCostumerNoAuth() throws Exception{
        mockMvc.perform(post("/customers/new")
                .param("customerName","teste customer"))
                .andExpect(status().isUnauthorized());
    }
}
