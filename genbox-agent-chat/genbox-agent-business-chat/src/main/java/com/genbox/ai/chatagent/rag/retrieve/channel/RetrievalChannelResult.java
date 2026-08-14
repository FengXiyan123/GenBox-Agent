package com.genbox.ai.chatagent.rag.retrieve.channel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.document.Document;

import java.util.List;

/**
 * 结果对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrievalChannelResult {

    private String channelName;

    private List<Document> documents;
}
