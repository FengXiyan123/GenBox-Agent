package com.genbox.ai.manage.support;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import com.genbox.enums.DocumentStructureNodeTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 支撑组件。
 */
@AllArgsConstructor
@Component
public class DocumentStructureNodeExtractor {

    private final DocumentStructureSignalExtractor signalExtractor;
    private final DocumentStructureAmbiguityResolver ambiguityResolver;
    private final DocumentStructureHierarchyResolver hierarchyResolver;
    private final DocumentStructureTreeValidator treeValidator;

    public List<DocumentStructureNodeCandidate> extract(String documentTitle, String parsedText) {
        String normalizedTitle = StrUtil.blankToDefault(documentTitle, "文档").trim();
        String normalizedText = StrUtil.blankToDefault(parsedText, "").trim();
        if (normalizedText.isBlank()) {
            return List.of(new DocumentStructureNodeCandidate(
                1,
                DocumentStructureNodeTypeEnum.DOCUMENT.getCode(),
                null,
                0,
                0,
                0,
                "",
                normalizedTitle,
                normalizedTitle,
                "/document",
                "",
                "",
                null
            ));
        }

        DocumentStructureSignalBatch signalBatch = signalExtractor.extract(normalizedTitle, normalizedText);
        List<DocumentStructureSignal> rawSignals = signalBatch == null ? List.of() : signalBatch.signals();
        List<String> allLines = signalBatch == null ? List.of() : signalBatch.contextLines();
        List<DocumentStructureSignal> resolvedSignals = ambiguityResolver.resolve(normalizedTitle, allLines, rawSignals);
        List<DocumentStructureNodeDraft> drafts = hierarchyResolver.resolve(normalizedTitle, resolvedSignals);
        return treeValidator.validateAndBuild(normalizedTitle, drafts);
    }
}
