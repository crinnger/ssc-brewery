package guru.sfg.brewery.web.controllers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class BeerControllerIT extends BaseIT{
    @Test
    void newBeer() throws Exception {
        mockMvc.perform(get("/api/v1/beer/8b032eef-c54a-4952-94a9-a14583254ab7")
                .with(httpBasic("kleyver", "oliveira")))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerHttpBasicUserRoleNoAuthuser() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/8b032eef-c54a-4952-94a9-a14583254ab7")
                .with(httpBasic("samuel", "oliveira")))
                .andExpect(status().isForbidden());
    }
    @Test
    void deleteBeerHttpBasicUserRoleNoAuthCostumer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/8b032eef-c54a-4952-94a9-a14583254ab7")
                .with(httpBasic("kleyver", "oliveira")))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBeerHttpBasicUserRoleAuthAdmin() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/8b032eef-c54a-4952-94a9-a14583254ab7")
                .with(httpBasic("crinnger", "oliveira")))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/8b032eef-c54a-4952-94a9-a14583254ab7"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void newBeerWithSecretNoAuth() throws Exception {
        mockMvc.perform(get("/beers/new").header("Api-key","kleyver")
                .header("Api-Secret","olveira"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void newBeerWithUrlCred() throws Exception {
        mockMvc.perform(get("/beers/new").param("Apikey","kleyver")
                .header("ApiSecret","oliveira"))
                .andExpect(status().isOk());
    }

    @Test
    void newBeerWithUrlBadCred() throws Exception {
        mockMvc.perform(get("/beers/new").param("Apikey","kleyver")
                .header("ApiSecret","olveira"))
                .andExpect(status().isOk());
    }



    @Test
    void newBeerWithSecret() throws Exception {
        mockMvc.perform(get("/beers/new").header("Api-key","kleyver")
                .header("Api-Secret","oliveira"))
                .andExpect(status().isOk());
    }

    @Test
    void breweryWithUserRole() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                .with(httpBasic("kleyver", "oliveira")))
                .andExpect(status().isOk());
    }

    @Test
    void breweryWithNoUserRole() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                .with(httpBasic("samuel", "oliveira")))
                .andExpect(status().isForbidden());
    }

    @Test
    void breweryWithNoAuth() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void breweryWithUserRoleAdmin() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                .with(httpBasic("crinnger", "oliveira")))
                .andExpect(status().is2xxSuccessful());
    }


    @Test
    void beerUpcWithUserRoleAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/11")
                .with(httpBasic("crinnger", "oliveira")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void beerUpcWithUserRoleUser() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/11")
                .with(httpBasic("samuel", "oliveira")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void beerUpcWithUserRoleCustomer() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/11")
                .with(httpBasic("kleyver", "oliveira")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void beerUpcWithNoAuth() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/11")
                .with(httpBasic("crinnger", "olivra")))
                .andExpect(status().isUnauthorized());
    }
}
