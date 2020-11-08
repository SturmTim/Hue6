/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htlgrieskirchen.po3.tsturm18;

/**
 *
 * @author timst
 */
public class TagString implements Comparable<TagString> {

    private int tagPriority;
    private String fullString;

    public TagString(int priority, String string) {
        this.tagPriority = priority;
        this.fullString = string;
    }

    public int getTagPriority() {
        return tagPriority;
    }

    public String getFullString() {
        return fullString;
    }

    public void setFullString(String string) {
        this.fullString = string;
    }

    public void setTagPriority(int tagPriority) {
        this.tagPriority = tagPriority;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.tagPriority;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TagString other = (TagString) obj;
        if (this.tagPriority != other.tagPriority) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(TagString o) {
        return this.tagPriority - o.tagPriority;
    }

}
