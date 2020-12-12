package apis.reddit;

import apis.imgur.ImgurUtil;
import apis.models.PostEntry;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.StringUtil;
import org.mockito.internal.util.io.IOUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 *
 * @author Mario Teklic
 */

class RedditUtilTest {

    @Test
    void getUrl() {
    }

    @Test
    void getTimestamp() {
    }

    @Test
    void getPosts() throws FileNotFoundException {
        ImgurUtil imgurUtil = new ImgurUtil();

        List<PostEntry> list = imgurUtil.getPosts(content);

        list.stream().forEach(postEntry -> System.out.println(postEntry));
    }

    @Test
    void hasNullObjects() {
    }

    @Test
    void isImageUploadAllowed() {
    }

    String content = "{\n" +
            "  \"kind\": \"Listing\",\n" +
            "  \"data\": {\n" +
            "    \"modhash\": \"z604iv746sb9746b584bad739276d91ab1ea5c829f275bfb27\",\n" +
            "    \"dist\": 25,\n" +
            "    \"children\": [\n" +
            "      {\n" +
            "        \"kind\": \"t3\",\n" +
            "        \"data\": {\n" +
            "          \"approved_at_utc\": null,\n" +
            "          \"subreddit\": \"nature\",\n" +
            "          \"selftext\": \"\",\n" +
            "          \"author_fullname\": \"t2_63uw6h9r\",\n" +
            "          \"saved\": false,\n" +
            "          \"mod_reason_title\": null,\n" +
            "          \"gilded\": 0,\n" +
            "          \"clicked\": false,\n" +
            "          \"title\": \"Humanity is waging a 'suicidal' war on nature, UN chief warns - \\\"Air and water pollution are killing 9 million people annually -- more than six times the current toll of the pandemic.\\\"\",\n" +
            "          \"link_flair_richtext\": [ ],\n" +
            "          \"subreddit_name_prefixed\": \"r/nature\",\n" +
            "          \"hidden\": false,\n" +
            "          \"pwls\": 6,\n" +
            "          \"link_flair_css_class\": null,\n" +
            "          \"downs\": 0,\n" +
            "          \"thumbnail_height\": 78,\n" +
            "          \"top_awarded_type\": null,\n" +
            "          \"hide_score\": false,\n" +
            "          \"name\": \"t3_k6iig6\",\n" +
            "          \"quarantine\": false,\n" +
            "          \"link_flair_text_color\": \"dark\",\n" +
            "          \"upvote_ratio\": 0.97,\n" +
            "          \"author_flair_background_color\": null,\n" +
            "          \"subreddit_type\": \"public\",\n" +
            "          \"ups\": 74,\n" +
            "          \"total_awards_received\": 0,\n" +
            "          \"media_embed\": { },\n" +
            "          \"thumbnail_width\": 140,\n" +
            "          \"author_flair_template_id\": null,\n" +
            "          \"is_original_content\": false,\n" +
            "          \"user_reports\": [ ],\n" +
            "          \"secure_media\": null,\n" +
            "          \"is_reddit_media_domain\": false,\n" +
            "          \"is_meta\": false,\n" +
            "          \"category\": null,\n" +
            "          \"secure_media_embed\": { },\n" +
            "          \"link_flair_text\": null,\n" +
            "          \"can_mod_post\": false,\n" +
            "          \"score\": 74,\n" +
            "          \"approved_by\": null,\n" +
            "          \"author_premium\": true,\n" +
            "          \"thumbnail\": \"https://b.thumbs.redditmedia.com/YufIF6k3sJkeInljliGf-uGMdb_INCX72eA7sfIyA3s.jpg\",\n" +
            "          \"edited\": false,\n" +
            "          \"author_flair_css_class\": null,\n" +
            "          \"author_flair_richtext\": [ ],\n" +
            "          \"gildings\": { },\n" +
            "          \"post_hint\": \"link\",\n" +
            "          \"content_categories\": null,\n" +
            "          \"is_self\": false,\n" +
            "          \"mod_note\": null,\n" +
            "          \"created\": 1.60710663E9,\n" +
            "          \"link_flair_type\": \"text\",\n" +
            "          \"wls\": 6,\n" +
            "          \"removed_by_category\": null,\n" +
            "          \"banned_by\": null,\n" +
            "          \"author_flair_type\": \"text\",\n" +
            "          \"domain\": \"cnn.com\",\n" +
            "          \"allow_live_comments\": false,\n" +
            "          \"selftext_html\": null,\n" +
            "          \"likes\": null,\n" +
            "          \"suggested_sort\": null,\n" +
            "          \"banned_at_utc\": null,\n" +
            "          \"url_overridden_by_dest\": \"https://www.cnn.com/2020/12/02/world/un-state-of-the-planet-guterres-speech-intl/index.html?utm_campaign=Hot%20News&amp;utm_medium=email&amp;_hsmi=101740057&amp;_hsenc=p2ANqtz-9qJ7ziohhmaURJ2q-iakorgAbK-wHq07lAiNlHpFaLnPYhed9ISYoJOKX83cDUdZTbm4H-Q6-mrU1v886ivtWP0X2pYQ&amp;utm_content=101740057&amp;utm_source=hs_email\",\n" +
            "          \"view_count\": null,\n" +
            "          \"archived\": false,\n" +
            "          \"no_follow\": false,\n" +
            "          \"is_crosspostable\": true,\n" +
            "          \"pinned\": false,\n" +
            "          \"over_18\": false,\n" +
            "          \"preview\": {\n" +
            "            \"images\": [\n" +
            "              {\n" +
            "                \"source\": {\n" +
            "                  \"url\": \"https://external-preview.redd.it/y99BwI48BIQYN2vlyK6nBa2L-Zj--n1xtQM_-8K3wBo.jpg?auto=webp&amp;s=ac138e9a1bb6bcedac2cb9021237eff342ee53c7\",\n" +
            "                  \"width\": 1100,\n" +
            "                  \"height\": 619\n" +
            "                },\n" +
            "                \"resolutions\": [\n" +
            "                  {\n" +
            "                    \"url\": \"https://external-preview.redd.it/y99BwI48BIQYN2vlyK6nBa2L-Zj--n1xtQM_-8K3wBo.jpg?width=108&amp;crop=smart&amp;auto=webp&amp;s=d82d50a60d160a968561947c47a9e04b8a3106f2\",\n" +
            "                    \"width\": 108,\n" +
            "                    \"height\": 60\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"url\": \"https://external-preview.redd.it/y99BwI48BIQYN2vlyK6nBa2L-Zj--n1xtQM_-8K3wBo.jpg?width=216&amp;crop=smart&amp;auto=webp&amp;s=d8460a21c010a63c365d62d260cbaf26f1aecc07\",\n" +
            "                    \"width\": 216,\n" +
            "                    \"height\": 121\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"url\": \"https://external-preview.redd.it/y99BwI48BIQYN2vlyK6nBa2L-Zj--n1xtQM_-8K3wBo.jpg?width=320&amp;crop=smart&amp;auto=webp&amp;s=83326e2bf7a332b7fddb116b7806b83da588d372\",\n" +
            "                    \"width\": 320,\n" +
            "                    \"height\": 180\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"url\": \"https://external-preview.redd.it/y99BwI48BIQYN2vlyK6nBa2L-Zj--n1xtQM_-8K3wBo.jpg?width=640&amp;crop=smart&amp;auto=webp&amp;s=eb50822b698374744566050293bc22627b3cfb37\",\n" +
            "                    \"width\": 640,\n" +
            "                    \"height\": 360\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"url\": \"https://external-preview.redd.it/y99BwI48BIQYN2vlyK6nBa2L-Zj--n1xtQM_-8K3wBo.jpg?width=960&amp;crop=smart&amp;auto=webp&amp;s=86ede821b904d3be7aaa93b8fc894f1d13e65d87\",\n" +
            "                    \"width\": 960,\n" +
            "                    \"height\": 540\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"url\": \"https://external-preview.redd.it/y99BwI48BIQYN2vlyK6nBa2L-Zj--n1xtQM_-8K3wBo.jpg?width=1080&amp;crop=smart&amp;auto=webp&amp;s=05f07cb34f2461cd675fab786698a5de6bd998df\",\n" +
            "                    \"width\": 1080,\n" +
            "                    \"height\": 607\n" +
            "                  }\n" +
            "                ],\n" +
            "                \"variants\": { },\n" +
            "                \"id\": \"c5JgrbNzAoMshdiFbGjYLwkbiAvSiygMthV-zZlm7JU\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"enabled\": false\n" +
            "          },\n" +
            "          \"all_awardings\": [ ],\n" +
            "          \"awarders\": [ ],\n" +
            "          \"media_only\": false,\n" +
            "          \"can_gild\": true,\n" +
            "          \"spoiler\": false,\n" +
            "          \"locked\": false,\n" +
            "          \"author_flair_text\": null,\n" +
            "          \"treatment_tags\": [ ],\n" +
            "          \"visited\": false,\n" +
            "          \"removed_by\": null,\n" +
            "          \"num_reports\": null,\n" +
            "          \"distinguished\": null,\n" +
            "          \"subreddit_id\": \"t5_2qh4c\",\n" +
            "          \"mod_reason_by\": null,\n" +
            "          \"removal_reason\": null,\n" +
            "          \"link_flair_background_color\": \"\",\n" +
            "          \"id\": \"k6iig6\",\n" +
            "          \"is_robot_indexable\": true,\n" +
            "          \"report_reasons\": null,\n" +
            "          \"author\": \"Facerealityalready\",\n" +
            "          \"discussion_type\": null,\n" +
            "          \"num_comments\": 9,\n" +
            "          \"send_replies\": true,\n" +
            "          \"whitelist_status\": \"all_ads\",\n" +
            "          \"contest_mode\": false,\n" +
            "          \"mod_reports\": [ ],\n" +
            "          \"author_patreon_flair\": false,\n" +
            "          \"author_flair_text_color\": null,\n" +
            "          \"permalink\": \"/r/nature/comments/k6iig6/humanity_is_waging_a_suicidal_war_on_nature_un/\",\n" +
            "          \"parent_whitelist_status\": \"all_ads\",\n" +
            "          \"stickied\": false,\n" +
            "          \"url\": \"https://www.cnn.com/2020/12/02/world/un-state-of-the-planet-guterres-speech-intl/index.html?utm_campaign=Hot%20News&amp;utm_medium=email&amp;_hsmi=101740057&amp;_hsenc=p2ANqtz-9qJ7ziohhmaURJ2q-iakorgAbK-wHq07lAiNlHpFaLnPYhed9ISYoJOKX83cDUdZTbm4H-Q6-mrU1v886ivtWP0X2pYQ&amp;utm_content=101740057&amp;utm_source=hs_email\",\n" +
            "          \"subreddit_subscribers\": 105368,\n" +
            "          \"created_utc\": 1.60707783E9,\n" +
            "          \"num_crossposts\": 0,\n" +
            "          \"media\": null,\n" +
            "          \"is_video\": false\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"kind\": \"t3\",\n" +
            "        \"data\": {\n" +
            "          \"approved_at_utc\": null,\n" +
            "          \"subreddit\": \"nature\",\n" +
            "          \"selftext\": \"\",\n" +
            "          \"author_fullname\": \"t2_4e88rhze\",\n" +
            "          \"saved\": false,\n" +
            "          \"mod_reason_title\": null,\n" +
            "          \"gilded\": 0,\n" +
            "          \"clicked\": false,\n" +
            "          \"title\": \"Unprecedented sightings of maned wolves in Amazon herald a changing landscape\",\n" +
            "          \"link_flair_richtext\": [ ],\n" +
            "          \"subreddit_name_prefixed\": \"r/nature\",\n" +
            "          \"hidden\": false,\n" +
            "          \"pwls\": 6,\n" +
            "          \"link_flair_css_class\": null,\n" +
            "          \"downs\": 0,\n" +
            "          \"thumbnail_height\": 93,\n" +
            "          \"top_awarded_type\": null,\n" +
            "          \"hide_score\": false,\n" +
            "          \"name\": \"t3_k5ufjd\",\n" +
            "          \"quarantine\": false,\n" +
            "          \"link_flair_text_color\": \"dark\",\n" +
            "          \"upvote_ratio\": 1.0,\n" +
            "          \"author_flair_background_color\": null,\n" +
            "          \"subreddit_type\": \"public\",\n" +
            "          \"ups\": 206,\n" +
            "          \"total_awards_received\": 1,\n" +
            "          \"media_embed\": { },\n" +
            "          \"thumbnail_width\": 140,\n" +
            "          \"author_flair_template_id\": null,\n" +
            "          \"is_original_content\": false,\n" +
            "          \"user_reports\": [ ],\n" +
            "          \"secure_media\": null,\n" +
            "          \"is_reddit_media_domain\": false,\n" +
            "          \"is_meta\": false,\n" +
            "          \"category\": null,\n" +
            "          \"secure_media_embed\": { },\n" +
            "          \"link_flair_text\": null,\n" +
            "          \"can_mod_post\": false,\n" +
            "          \"score\": 206,\n" +
            "          \"approved_by\": null,\n" +
            "          \"author_premium\": false,\n" +
            "          \"thumbnail\": \"https://b.thumbs.redditmedia.com/uGxpPKN64qZt1S18In3qqlVrLnxDAkJKj6HA50uJcgc.jpg\",\n" +
            "          \"edited\": false,\n" +
            "          \"author_flair_css_class\": null,\n" +
            "          \"author_flair_richtext\": [ ],\n" +
            "          \"gildings\": { },\n" +
            "          \"post_hint\": \"link\",\n" +
            "          \"content_categories\": null,\n" +
            "          \"is_self\": false,\n" +
            "          \"mod_note\": null,\n" +
            "          \"created\": 1.607018231E9,\n" +
            "          \"link_flair_type\": \"text\",\n" +
            "          \"wls\": 6,\n" +
            "          \"removed_by_category\": null,\n" +
            "          \"banned_by\": null,\n" +
            "          \"author_flair_type\": \"text\",\n" +
            "          \"domain\": \"news.mongabay.com\",\n" +
            "          \"allow_live_comments\": false,\n" +
            "          \"selftext_html\": null,\n" +
            "          \"likes\": null,\n" +
            "          \"suggested_sort\": null,\n" +
            "          \"banned_at_utc\": null,\n" +
            "          \"url_overridden_by_dest\": \"https://news.mongabay.com/2020/12/unprecedented-sightings-of-maned-wolves-in-amazon-herald-a-changing-landscape/\",\n" +
            "          \"view_count\": null,\n" +
            "          \"archived\": false,\n" +
            "          \"no_follow\": false,\n" +
            "          \"is_crosspostable\": true,\n" +
            "          \"pinned\": false,\n" +
            "          \"over_18\": false,\n" +
            "          \"preview\": {\n" +
            "            \"images\": [\n" +
            "              {\n" +
            "                \"source\": {\n" +
            "                  \"url\": \"https://external-preview.redd.it/BI3fbjQjF9-K_4BeDBZDEG4jjEGcmToBuqgl_6RMnhU.jpg?auto=webp&amp;s=d2c559dd98adf96bb65243ff532ace76fb1d754e\",\n" +
            "                  \"width\": 768,\n" +
            "                  \"height\": 512\n" +
            "                },\n" +
            "                \"resolutions\": [\n" +
            "                  {\n" +
            "                    \"url\": \"https://external-preview.redd.it/BI3fbjQjF9-K_4BeDBZDEG4jjEGcmToBuqgl_6RMnhU.jpg?width=108&amp;crop=smart&amp;auto=webp&amp;s=58aba796292a9873c894a6311faef54237e71639\",\n" +
            "                    \"width\": 108,\n" +
            "                    \"height\": 72\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"url\": \"https://external-preview.redd.it/BI3fbjQjF9-K_4BeDBZDEG4jjEGcmToBuqgl_6RMnhU.jpg?width=216&amp;crop=smart&amp;auto=webp&amp;s=4d257b7e91286ae4463e615277a209fbdb80df2d\",\n" +
            "                    \"width\": 216,\n" +
            "                    \"height\": 144\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"url\": \"https://external-preview.redd.it/BI3fbjQjF9-K_4BeDBZDEG4jjEGcmToBuqgl_6RMnhU.jpg?width=320&amp;crop=smart&amp;auto=webp&amp;s=c73c737cfa37dff4ad7eea3783fb72cfa5464ad1\",\n" +
            "                    \"width\": 320,\n" +
            "                    \"height\": 213\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"url\": \"https://external-preview.redd.it/BI3fbjQjF9-K_4BeDBZDEG4jjEGcmToBuqgl_6RMnhU.jpg?width=640&amp;crop=smart&amp;auto=webp&amp;s=952a60acf0a03a3d08062786e80abd57d456a70e\",\n" +
            "                    \"width\": 640,\n" +
            "                    \"height\": 426\n" +
            "                  }\n" +
            "                ],\n" +
            "                \"variants\": { },\n" +
            "                \"id\": \"L1SXWUMHuNtmakGO9_IzvgnnvwBs6lo3rkcCQ4SR_rw\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"enabled\": false\n" +
            "          },\n" +
            "          \"all_awardings\": [\n" +
            "            {\n" +
            "              \"giver_coin_reward\": null,\n" +
            "              \"subreddit_id\": null,\n" +
            "              \"is_new\": false,\n" +
            "              \"days_of_drip_extension\": 0,\n" +
            "              \"coin_price\": 300,\n" +
            "              \"id\": \"award_28e8196b-d4e9-45bc-b612-cd4c7d3ed4b3\",\n" +
            "              \"penny_donate\": null,\n" +
            "              \"award_sub_type\": \"GLOBAL\",\n" +
            "              \"coin_reward\": 0,\n" +
            "              \"icon_url\": \"https://i.redd.it/award_images/t5_22cerq/94pn64yuas941_RocketLike.png\",\n" +
            "              \"days_of_premium\": 0,\n" +
            "              \"tiers_by_required_awardings\": null,\n" +
            "              \"resized_icons\": [\n" +
            "                {\n" +
            "                  \"url\": \"https://preview.redd.it/award_images/t5_22cerq/94pn64yuas941_RocketLike.png?width=16&amp;height=16&amp;auto=webp&amp;s=c1400eb6ea235d0c96c3aa6e271c71d7f339cbd4\",\n" +
            "                  \"width\": 16,\n" +
            "                  \"height\": 16\n" +
            "                },\n" +
            "                {\n" +
            "                  \"url\": \"https://preview.redd.it/award_images/t5_22cerq/94pn64yuas941_RocketLike.png?width=32&amp;height=32&amp;auto=webp&amp;s=77ad345b2f9b062140e028764394594326771a17\",\n" +
            "                  \"width\": 32,\n" +
            "                  \"height\": 32\n" +
            "                },\n" +
            "                {\n" +
            "                  \"url\": \"https://preview.redd.it/award_images/t5_22cerq/94pn64yuas941_RocketLike.png?width=48&amp;height=48&amp;auto=webp&amp;s=5b5211166e4b260311ad9f3ea31d3b815110769c\",\n" +
            "                  \"width\": 48,\n" +
            "                  \"height\": 48\n" +
            "                },\n" +
            "                {\n" +
            "                  \"url\": \"https://preview.redd.it/award_images/t5_22cerq/94pn64yuas941_RocketLike.png?width=64&amp;height=64&amp;auto=webp&amp;s=bf3a2c642ad50547087d770c65c29777970d3af3\",\n" +
            "                  \"width\": 64,\n" +
            "                  \"height\": 64\n" +
            "                },\n" +
            "                {\n" +
            "                  \"url\": \"https://preview.redd.it/award_images/t5_22cerq/94pn64yuas941_RocketLike.png?width=128&amp;height=128&amp;auto=webp&amp;s=eae06d6a70c62c78dc66cb14f2a84651cb822cc4\",\n" +
            "                  \"width\": 128,\n" +
            "                  \"height\": 128\n" +
            "                }\n" +
            "              ],\n" +
            "              \"icon_width\": 2048,\n" +
            "              \"static_icon_width\": 2048,\n" +
            "              \"start_date\": null,\n" +
            "              \"is_enabled\": true,\n" +
            "              \"awardings_required_to_grant_benefits\": null,\n" +
            "              \"description\": \"When an upvote just isn't enough, smash the Rocket Like.\",\n" +
            "              \"end_date\": null,\n" +
            "              \"subreddit_coin_reward\": 0,\n" +
            "              \"count\": 1,\n" +
            "              \"static_icon_height\": 2048,\n" +
            "              \"name\": \"Rocket Like\",\n" +
            "              \"resized_static_icons\": [\n" +
            "                {\n" +
            "                  \"url\": \"https://preview.redd.it/award_images/t5_22cerq/94pn64yuas941_RocketLike.png?width=16&amp;height=16&amp;auto=webp&amp;s=c1400eb6ea235d0c96c3aa6e271c71d7f339cbd4\",\n" +
            "                  \"width\": 16,\n" +
            "                  \"height\": 16\n" +
            "                },\n" +
            "                {\n" +
            "                  \"url\": \"https://preview.redd.it/award_images/t5_22cerq/94pn64yuas941_RocketLike.png?width=32&amp;height=32&amp;auto=webp&amp;s=77ad345b2f9b062140e028764394594326771a17\",\n" +
            "                  \"width\": 32,\n" +
            "                  \"height\": 32\n" +
            "                },\n" +
            "                {\n" +
            "                  \"url\": \"https://preview.redd.it/award_images/t5_22cerq/94pn64yuas941_RocketLike.png?width=48&amp;height=48&amp;auto=webp&amp;s=5b5211166e4b260311ad9f3ea31d3b815110769c\",\n" +
            "                  \"width\": 48,\n" +
            "                  \"height\": 48\n" +
            "                },\n" +
            "                {\n" +
            "                  \"url\": \"https://preview.redd.it/award_images/t5_22cerq/94pn64yuas941_RocketLike.png?width=64&amp;height=64&amp;auto=webp&amp;s=bf3a2c642ad50547087d770c65c29777970d3af3\",\n" +
            "                  \"width\": 64,\n" +
            "                  \"height\": 64\n" +
            "                },\n" +
            "                {\n" +
            "                  \"url\": \"https://preview.redd.it/award_images/t5_22cerq/94pn64yuas941_RocketLike.png?width=128&amp;height=128&amp;auto=webp&amp;s=eae06d6a70c62c78dc66cb14f2a84651cb822cc4\",\n" +
            "                  \"width\": 128,\n" +
            "                  \"height\": 128\n" +
            "                }\n" +
            "              ],\n" +
            "              \"icon_format\": null,\n" +
            "              \"icon_height\": 2048,\n" +
            "              \"penny_price\": null,\n" +
            "              \"award_type\": \"global\",\n" +
            "              \"static_icon_url\": \"https://i.redd.it/award_images/t5_22cerq/94pn64yuas941_RocketLike.png\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"awarders\": [ ],\n" +
            "          \"media_only\": false,\n" +
            "          \"can_gild\": true,\n" +
            "          \"spoiler\": false,\n" +
            "          \"locked\": false,\n" +
            "          \"author_flair_text\": null,\n" +
            "          \"treatment_tags\": [ ],\n" +
            "          \"visited\": false,\n" +
            "          \"removed_by\": null,\n" +
            "          \"num_reports\": null,\n" +
            "          \"distinguished\": null,\n" +
            "          \"subreddit_id\": \"t5_2qh4c\",\n" +
            "          \"mod_reason_by\": null,\n" +
            "          \"removal_reason\": null,\n" +
            "          \"link_flair_background_color\": \"\",\n" +
            "          \"id\": \"k5ufjd\",\n" +
            "          \"is_robot_indexable\": true,\n" +
            "          \"report_reasons\": null,\n" +
            "          \"author\": \"Sorin61\",\n" +
            "          \"discussion_type\": null,\n" +
            "          \"num_comments\": 5,\n" +
            "          \"send_replies\": true,\n" +
            "          \"whitelist_status\": \"all_ads\",\n" +
            "          \"contest_mode\": false,\n" +
            "          \"mod_reports\": [ ],\n" +
            "          \"author_patreon_flair\": false,\n" +
            "          \"author_flair_text_color\": null,\n" +
            "          \"permalink\": \"/r/nature/comments/k5ufjd/unprecedented_sightings_of_maned_wolves_in_amazon/\",\n" +
            "          \"parent_whitelist_status\": \"all_ads\",\n" +
            "          \"stickied\": false,\n" +
            "          \"url\": \"https://news.mongabay.com/2020/12/unprecedented-sightings-of-maned-wolves-in-amazon-herald-a-changing-landscape/\",\n" +
            "          \"subreddit_subscribers\": 105368,\n" +
            "          \"created_utc\": 1.606989431E9,\n" +
            "          \"num_crossposts\": 0,\n" +
            "          \"media\": null,\n" +
            "          \"is_video\": false\n" +
            "        }\n" +
            "      }\n" +
            "      ],\n" +
            "    \"after\": \"t3_k3xgy0\",\n" +
            "    \"before\": null\n" +
            "  }\n" +
            "}";
}
