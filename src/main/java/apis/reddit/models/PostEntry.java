/*
 * Copyright (c) 2020
 * Contributed by NAME HERE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package apis.reddit.models;
/*
 *
 * @author Mario Teklic
 */

/**
 * Represents a post from reddit
 *
 */
public class PostEntry implements Comparable<PostEntry>{

    /**
     * A downloadable URL for a media i.e. an image
     */
    private String url;

    /**
     * The timestamp, when the post was made as a long value
     * There is no setter because the timestamp should not be changeable
     */
    private final MyDate date;

    public PostEntry(String url, MyDate date){
        this.url = url;
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MyDate getDate() {
        return date;
    }

    /**
     *
     * @param postEntry
     * @return 1 if equal
     * @return -1 if not equal
     */
    @Override
    public int compareTo(PostEntry postEntry) {
        return this.getDate().compareTo(postEntry.getDate());
    }
}
