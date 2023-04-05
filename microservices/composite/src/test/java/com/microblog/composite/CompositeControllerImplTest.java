package com.microblog.composite;

import com.microblog.composite.controller.CompositeControllerImpl;
import core.comment.Comment;
import core.post.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
public class CompositeControllerImplTest {

    @MockBean
    WebClient.Builder webClientBuilder;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        webTestClient = WebTestClient.bindToController(new CompositeControllerImpl(webClientBuilder))
                .configureClient()
                .filter(WebTestClientRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void getPostTest() {
        int postId = 12;
        Post post = new Post(postId, "post title " + postId, "post author " + postId, "contents " + postId);
        List<Comment> comments = List.of(
                new Comment(postId, 1, "작성자1", "내용1"),
                new Comment(postId, 2, "작성자2", "내용2")
        );

        WebClient webClient = Mockito.mock(WebClient.class, Answers.RETURNS_DEEP_STUBS);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(
                webClient.get()
                        .uri(ArgumentMatchers.anyString())
                        .retrieve()
                        .bodyToMono(Post.class)
                        .log()
        ).thenReturn(Mono.just(post));

        when(
                webClient.get()
                        .uri(ArgumentMatchers.anyString())
                        .retrieve()
                        .bodyToFlux(Comment.class)
                        .log()
        ).thenReturn(Flux.fromIterable(comments));

        webTestClient.get()
                .uri("/composite/{postId}", postId)
                .attribute("org.springframework.restdocs.urlTemplate", "/composite/{postId}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("composite",
                        pathParameters(
                                RequestDocumentation.parameterWithName("postId").description("post id")
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
                ));

    }

}

