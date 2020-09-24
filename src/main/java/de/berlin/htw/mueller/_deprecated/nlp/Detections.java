package de.berlin.htw.mueller._deprecated.nlp;

import org.springframework.stereotype.Service;

@Service
public class Detections {
    /*
    public String[] find(String str) throws IOException {
        String[] sentences = getSentences(str);
        return Arrays.stream(sentences)
                .map(sentence -> {
                    try {
                        return tokenize(sentence);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .flatMap(Stream::of)
                .toArray(String[]::new);
    }

    public String[] doPOSTagging(String[] tokens) throws IOException {
        InputStream inputStreamPOSTagger = getClass()
                .getResourceAsStream("/models/de-pos-maxent.bin");
        POSModel posModel = new POSModel(inputStreamPOSTagger);
        POSTaggerME posTagger = new POSTaggerME(posModel);
        return posTagger.tag(tokens);
    }

    public String[] lemmatize(String[] tokens, String[] tags) throws IOException {
        InputStream dictLemmatizer = getClass()
                .getResourceAsStream("/models/dictionaries/de-lemmatizer.dict");
        DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(
                dictLemmatizer);
        return lemmatizer.lemmatize(tokens, tags);
    }

    public String[] getSentences(String str) throws IOException {
        InputStream is = getClass().getResourceAsStream("/models/de-sent.bin");
        SentenceModel model = new SentenceModel(is);
        SentenceDetectorME sdetector = new SentenceDetectorME(model);
        return sdetector.sentDetect(str);
    }

    public String[] tokenize(String sentence) throws IOException {
        InputStream inputStream = getClass()
                .getResourceAsStream("/models/de-token.bin");
        TokenizerModel model = new TokenizerModel(inputStream);
        TokenizerME tokenizer = new TokenizerME(model);
        return tokenizer.tokenize(sentence);
    }

     */
}
