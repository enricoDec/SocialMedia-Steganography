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

package socialmediasteganography;import apis.SocialMedia;

import java.util.List;

public interface SocialMediaSteganography {

    /**
     * Encodes payload in carrier and posts the result to socialMedia
     * @param socialMedia Social Media
     * @param carrier data used to encode payload in
     * @param payload payload to encode
     * @return
     */
    boolean encodeAndPost(SocialMedia socialMedia, byte[] carrier, byte[] payload);

    /**
     * Searches a specific Social Media for hidden messages
     * @return decoded hidden messages
     */
    List<byte[]> searchForHiddenMessages(SocialMedia socialMedia, String keyword);

}
