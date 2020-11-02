/*
 * Copyright (c) 2020
 * Contributed by Mario Teklic
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

import java.util.List;

/**
 * Represens a JSON response from reddit.com as a Java Model.
 * Has several properties which are not used for this application at this point.
 * Maybe they can be usefull later, therefore they should be kept.
 */
public class RedditGetResponse {
    private String kind;
    private ResponseData data;

    public String getKind() {
        return kind;
    }

    public ResponseData getData() {
        return data;
    }

    public static class ResponseData {
        private String modhash;
        private List<ResponseChildData> children;
        private String after;
        private String before;

        public String getModhash() {
            return modhash;
        }


        public List<ResponseChildData> getChildren() {
            return children;
        }

        public String getAfter() {
            return after;
        }

        public String getBefore() {
            return before;
        }
    }

    public static class ResponseChildData {
        private String kind;
        private ChildData data;

        public String getKind() {
            return kind;
        }

        public ChildData getData() {
            return data;
        }

        public void setData(ChildData data) {
            this.data = data;
        }
    }

    public static class ChildData {
        private String url_overridden_by_dest;
        private PreviewData preview;
        private String domain;
        private String banned_by;
        private Object media_embed;
        private String subreddit;
        private String selftext_html;
        private String selftext;
        private String likes;
        private String link_flair_text;
        private String id;
        private boolean clicked;
        private String title;
        private int num_comments;
        private int score;
        private String approved_by;
        private boolean over_18;
        private boolean hidden;
        private String thumbnail;
        private String subreddit_id;
        private boolean edited;
        private boolean link_flair_css_class;
        private boolean author_flair_css_class;
        private int downs;
        private boolean saved;
        private boolean is_self;
        private String permalink;
        private String name;
        private String created;
        private String url;
        private String author_flair_text;
        private String author;
        private String created_utc;
        private String media;
        private String num_reports;
        private int ups;

        public String getUrl_overridden_by_dest() {
            return url_overridden_by_dest;
        }

        public void setUrl_overridden_by_dest(String url_overridden_by_dest) {
            this.url_overridden_by_dest = url_overridden_by_dest;
        }

        public PreviewData getPreview() {
            return preview;
        }

        public String getDomain() {
            return domain;
        }

        public String getBanned_by() {
            return banned_by;
        }

        public Object getMedia_embed() {
            return media_embed;
        }

        public String getSubreddit() {
            return subreddit;
        }

        public String getSelftext_html() {
            return selftext_html;
        }
        public String getSelftext() {
            return selftext;
        }

        public String getLikes() {
            return likes;
        }

        public String getLink_flair_text() {
            return link_flair_text;
        }

        public String getId() {
            return id;
        }

        public boolean isClicked() {
            return clicked;
        }

        public String getTitle() {
            return title;
        }

        public int getNum_comments() {
            return num_comments;
        }

        public int getScore() {
            return score;
        }

        public String getApproved_by() {
            return approved_by;
        }

        public boolean isOver_18() {
            return over_18;
        }

        public boolean isHidden() {
            return hidden;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public String getSubreddit_id() {
            return subreddit_id;
        }

        public boolean isEdited() {
            return edited;
        }

        public boolean isLink_flair_css_class() {
            return link_flair_css_class;
        }

        public boolean isAuthor_flair_css_class() {
            return author_flair_css_class;
        }

        public int getDowns() {
            return downs;
        }

        public boolean isSaved() {
            return saved;
        }

        public boolean isIs_self() {
            return is_self;
        }

        public String getPermalink() {
            return permalink;
        }

        public String getName() {
            return name;
        }

        public String getCreated() {
            return created;
        }

        public String getUrl() {
            return url;
        }

        public String getAuthor_flair_text() {
            return author_flair_text;
        }

        public String getAuthor() {
            return author;
        }

        public String getCreated_utc() {
            return created_utc;
        }

        public void setCreated_utc(String created_utc) {
            this.created_utc = created_utc;
        }

        public String getMedia() {
            return media;
        }

        public String getNum_reports() {
            return num_reports;
        }

        public int getUps() {
            return ups;
        }
    }

    public static class PreviewData{
        private List<ImageData> images;

        /**
         * Returns first Imageentry, which ist the source image.
         * @return
         */
        public ImageData getImages() {
            return images.get(0);
        }
    }

    public static class ImageData{
        private SourceData source;

        public SourceData getSource() {
            return source;
        }
    }

    public static class SourceData{
        private String url;

        public String getUrl() {
            return url;
        }
    }
}

