package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import panda.user.CpnExampleVocab;

public class Vocab extends Card {

    private String word;
    private String meaning;
    private String ipa;
    private String partOfSpeech;
    private int nWrite;
    private int nWriteMissed;
    //private int nListen;
    //private int nListenMissed;
    private String audio;
    private String image;
    private String video;
    private ArrayList<Sentence> sentence;
    public static final ArrayList<String> partofspeeches = new ArrayList<>(Arrays.asList(
            "noun",
            "pronoun",
            "verb",
            "adjective",
            "adverb",
            "preposition",
            "conjunction",
            "interjection",
            "Phrasal verb",
            "Idioms"
    ));

    public Vocab() {
        nWrite = 0;
        nWriteMissed = 0;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getIpa() {
        return ipa;
    }

    public void setIpa(String ipa) {
        this.ipa = ipa;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public int getnWrite() {
        return nWrite;
    }

    public void setnWrite(int nWrite) {
        this.nWrite = nWrite;
    }

    public int getnWriteMissed() {
        return nWriteMissed;
    }

    public void setnWriteMissed(int nWriteMissed) {
        this.nWriteMissed = nWriteMissed;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public ArrayList<Sentence> getSentence() {
        return sentence;
    }

    public void setSentence(ArrayList<Sentence> sentence) {
        this.sentence = sentence;
    }
}
