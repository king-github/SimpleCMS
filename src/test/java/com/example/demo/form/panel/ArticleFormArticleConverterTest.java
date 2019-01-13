package com.example.demo.form.panel;

import com.example.demo.entity.Article;
import com.example.demo.entity.Author;
import com.example.demo.entity.Section;
import com.example.demo.entity.Tag;
import com.example.demo.repository.SectionRepository;
import com.example.demo.repository.TagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ArticleFormArticleConverterTest {

    @Mock
    SectionRepository sectionRepository;

    @Mock
    TagRepository tagRepository;

    @InjectMocks
    ArticleFormArticleConverter articleFormArticleConverter;

    @Mock
    Author author1;

    Section section1;
    Section section2;

    List<Tag> tags;
    List<Tag> tags2;
    Set<Long> tagIds;

    ArticleForm articleFormBase;
    Article articleBase;

    LocalDateTime localDT1;
    LocalDateTime localDT2;
    LocalDateTime localDT3;

    @Before
    public void setUp() throws Exception {

        tagIds = new HashSet<>(Arrays.asList(2L, 3L, 4L));

        localDT1 = LocalDateTime.of(2017, 10, 10, 10, 20, 28);
        localDT2 = LocalDateTime.of(2018, 11, 10, 10, 20, 28);
        localDT3 = LocalDateTime.of(2018, 12, 10, 10, 20, 28);
        LocalDateTime publicationDT = LocalDateTime.of(2018, 11, 11, 10, 20, 28);
        LocalDateTime createDT = LocalDateTime.of(2018, 11, 10, 12, 40, 39);
        LocalDateTime lastUpdateDT = LocalDateTime.of(2018, 11, 12, 15, 20, 49);

        section1 = new Section("section1");
        section1.setId(7L);
        section2 = new Section("section2");
        section2.setId(24L);

        tags = Arrays.asList(new Tag(11L,"tag 1"),
                             new Tag(12L, "tag 2"),
                             new Tag(13L, "tag 3"));

        articleFormBase = new ArticleForm(11L, "Alf Alfa", "Title 11",
                                          "Lead text", "Content text", false,
                                          7L, tagIds, publicationDT, createDT, lastUpdateDT);

        tags2 = IntStream.range(6, 9).mapToObj(v -> {
            Tag tag = new Tag("Tag "+v);
            tag.setId(Long.valueOf(v));
            return tag;
        }).collect(Collectors.toList());

        articleBase =  new Article("Title 8", "Lead 8", "Content 8", author1);
        articleBase.setId(8L);
        articleBase.setSection(section2);
        articleBase.setTags(new HashSet<>(tags2));
        articleBase.setPublished(true);
        articleBase.setPublicationDate(localDT1);
        articleBase.setCreateDateTime(localDT2);
        articleBase.setUpdateDateTime(localDT3);
    }

    @Test
    public void givenArticleFormThenReceiveNewArticle() {

        when(sectionRepository.findById(anyLong())).thenReturn(Optional.of(section1));

        when(tagRepository.findAllById(tagIds)).thenReturn(tags);

        Article result = articleFormArticleConverter.toNewArticle(articleFormBase);

        assertEquals("Title 11", result.getTitle());
        assertEquals("Lead text", result.getLead());
        assertEquals("Content text", result.getContent());

        assertEquals(section1, result.getSection());
        assertThat(tags, containsInAnyOrder(result.getTags().toArray()));

        assertNull(result.getPublicationDate());
        assertNull(result.getCreateDateTime());
        assertNull(result.getUpdateDateTime());

        assertNull(result.getId());
        assertNull(result.getAuthor());
        assertFalse(result.isPublished());
    }

    @Test
    public void givenArticleFormAndArticleBaseThenReceiveArticle() {

        when(sectionRepository.findById(anyLong())).thenReturn(Optional.of(section1));

        when(tagRepository.findAllById(tagIds)).thenReturn(tags);

        Article result = articleFormArticleConverter.toArticle(articleFormBase, articleBase);

        assertEquals("Title 11", result.getTitle());
        assertEquals("Lead text", result.getLead());
        assertEquals("Content text", result.getContent());

        assertEquals(section1, result.getSection());
        assertThat(tags, containsInAnyOrder(result.getTags().toArray()));

        assertEquals(localDT1, result.getPublicationDate());
        assertEquals(localDT2, result.getCreateDateTime());
        assertEquals(localDT3, result.getUpdateDateTime());

        assertEquals(8L, result.getId().longValue());
        assertEquals(author1, result.getAuthor());
        assertEquals(false, result.isPublished());
    }

    @Test
    public void givenArticleBaseAndReciveArticleForm() {

        ArticleForm articleForm = articleFormArticleConverter.toArticleForm(articleBase);

        assertEquals(8L, articleForm.getId().longValue());

        assertEquals("Title 8", articleForm.getTitle());
        assertEquals("Lead 8", articleForm.getLead());
        assertEquals("Content 8", articleForm.getContent());

        assertEquals(true, articleForm.isPublished());

        assertEquals(24L, articleForm.getSectionId().longValue());
        assertThat(Arrays.asList(6L, 7L, 8L), containsInAnyOrder(articleForm.getTagIds().toArray()));

    }
}