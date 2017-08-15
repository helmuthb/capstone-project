package at.breitenfellner.roomquestions.util;

/**
 * This interface provides long ID values for string keys. Every key will always get the same ID,
 * and every key will get an ID different from other keys.
 */

public interface KeyIdSource {
    long getId(String key);
}
