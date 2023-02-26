package com.microblog.composite;

import com.microblog.composite.controller.CompositeControllerImpl;
import core.comment.Comment;
import core.post.Post;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CompositeControllerImpl.class)
@AutoConfigureRestDocs
public class CompositeControllerImplTest {
    @MockBean
    RestTemplate restTemplate;
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    MockMvc mockMvc;

    @Test
    public void getPostTest() throws Exception {
        int postId = 12;
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.eq(Post.class)))
                .thenReturn(new Post(postId, "post title " + postId, "post author " + postId, "contents " + postId));
        when(
                restTemplate.exchange(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.eq(HttpMethod.GET),
                        ArgumentMatchers.isNull(),
                        ArgumentMatchers.eq(new ParameterizedTypeReference<List<Comment>>() {
                        })
                )
        ).thenReturn(
                ResponseEntity.of(Optional.of(
                        List.of(
                                new Comment(postId, 1, "작성자1", "내용1"),
                                new Comment(postId, 2, "작성자2", "내용2")
                        )
                ))
        );

        // get("/composite/" + postId))
        mockMvc.perform(RestDocumentationRequestBuilders.get("/composite/{postId}", postId))
                // .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("composite",
                                // Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
//                                requestFields(
//                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("post id")
//                                ),
                                pathParameters(
                                        parameterWithName("postId").description("post id")
                                ),
                                responseFields(
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("post id"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("post title"),
                                        fieldWithPath("author").type(JsonFieldType.STRING).description("post author"),
                                        fieldWithPath("contents").type(JsonFieldType.STRING).description("post contents"),
                                        fieldWithPath("comments").type(JsonFieldType.ARRAY).description("post comments"),
                                        fieldWithPath("comments[].postId").type(JsonFieldType.NUMBER).description("comment's post id"),
                                        fieldWithPath("comments[].commentId").type(JsonFieldType.NUMBER).description("comment id"),
                                        fieldWithPath("comments[].author").type(JsonFieldType.STRING).description("comment author"),
                                        fieldWithPath("comments[].comment").type(JsonFieldType.STRING).description("comment")
                                )

                        )
                );
    }

}
