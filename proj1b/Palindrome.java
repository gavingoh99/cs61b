public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        LinkedListDeque<Character> deque = new LinkedListDeque<>();
        for (int index = 0; index < word.length(); index++) {
            char character = word.charAt(index);
            deque.addLast(character);
        }
        return deque;
    }
    public boolean isPalindrome(String word) {
        return helper(wordToDeque(word));
    }
//        if (word.length() <= 1) {
//            return true;
//        }
//        word = word.toLowerCase();
//        Deque<Character> deque = wordToDeque(word);
//        while (!deque.isEmpty()) {
//            if (Character.compare(deque.removeFirst(), deque.removeLast()) != 0) {
//                return false;
//            }
//            if (deque.size() == 1) {
//                break;
//            }
//        }
//        return true;
    private boolean helper(Deque<Character> deque) {
        if (deque.size() == 1 || deque.size() == 0) {
            return true;
        }
        if (Character.compare(deque.removeFirst(), deque.removeLast()) != 0) {
            return false;
        }
        return helper(deque);
    }
    public boolean isPalindrome(String word, CharacterComparator cc) {
        return helper(wordToDeque(word), cc);
    }
    private boolean helper(Deque<Character> deque, CharacterComparator cc) {
        if (deque.size() == 0 || deque.size() == 1) {
            return true;
        }
        if (!cc.equalChars(deque.removeFirst(), deque.removeLast())) {
            return false;
        }
        return helper(deque, cc);
    }
}
