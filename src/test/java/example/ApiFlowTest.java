package example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiFlowTest {

    @Autowired
    private MockMvc mvc;

    private void create(String url, String json) throws Exception {
        mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void createGetDeleteWithRelations() throws Exception {
        create("/social-network", "{\"name\":\"Insta\"}");
        create("/person", "{\"phoneNumber\":\"123\",\"socialNetworks\":[{\"id\":1}]}");
        create("/account", "{\"firstName\":\"Ivan\",\"lastName\":\"Ivanov\",\"socialNetwork\":{\"id\":1}}");

        mvc.perform(get("/social-network/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Insta"))
                .andExpect(jsonPath("$.persons[0].phoneNumber").value("123"))
                .andExpect(jsonPath("$.accounts[0].firstName").value("Ivan"));
        mvc.perform(get("/person/2")).andExpect(status().isOk())
                .andExpect(jsonPath("$.socialNetworks[0].name").value("Insta"));
        mvc.perform(get("/account/3")).andExpect(status().isOk())
                .andExpect(jsonPath("$.socialNetwork.name").value("Insta"));
        mvc.perform(get("/account")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));

        // удаление соцсети чистит join-таблицу и каскадно удаляет аккаунты, человек остаётся
        mvc.perform(delete("/social-network/1")).andExpect(status().isNoContent());
        mvc.perform(get("/account/3")).andExpect(status().isNotFound());
        mvc.perform(get("/person/2")).andExpect(status().isOk())
                .andExpect(jsonPath("$.socialNetworks.length()").value(0));

        mvc.perform(delete("/person/2")).andExpect(status().isNoContent());
        mvc.perform(delete("/person/2")).andExpect(status().isNotFound());
    }
}
