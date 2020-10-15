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

import apis.SocialMedia;

public interface SocialMediaSteganography {

    /**
     * Send address to all users
     * @param socialMedia Social Media
     * @param carrier data used to hide address
     * @param payload address
     * @return
     */
    boolean sendAddress(SocialMedia socialMedia, byte[] carrier, byte[] payload);

    /**
     * Search all Social Medias for addresses
     * @return addresses
     */
    byte[] receiveAllAddress();

    /**
     * Search a specific Social Media for addresses
     * @return addresses
     */
    byte[] receiveAddress(SocialMedia socialMedia);

}
