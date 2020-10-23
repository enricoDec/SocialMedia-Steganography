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

import java.util.Date;

public class MyDate implements Comparable{
    private Date date;

    /**
     * Date components.
     * Example: Okt. 23, 2020 4:33:27 NACHM
     * <p>
     * Getter will be modified to ints and booleans for month etc.
     * <p>
     * [0] Month - Will be changed to an integer. January = 1, December = 12
     * [1] Day - cut ','
     * [2] Year
     * [3] Hour - Minute - Second
     * [4] AM/PM
     */
    private String[] components;

    public MyDate(Date date) {
        this.date = date;
        this.components = date.toString().split(" ");
    }

    public String toString() {
        return date.toString();
    }

    /**
     * * [0] Month - Will be changed to an integer. January = 1, December = 12
     * * [1] Day - cut ','
     * * [2] Year
     * * [3] Hour - Minute - Second
     * * [4] AM/PM
     */

    public Integer getMonth() {
        switch (this.components[0]) {
            case "Jan.":
                return 1;
            case "Feb.":
                return 2;
            case "Mrz.":
                return 3;
            case "Apr.":
                return 4;
            case "Mai.":
                return 5;
            case "Jun.":
                return 6;
            case "Jul.":
                return 7;
            case "Aug.":
                return 8;
            case "Sep.":
                return 9;
            case "Okt.":
                return 10;
            case "Nov.":
                return 11;
            case "Dez.":
                return 12;
            default:
                return -1;
        }
    }

    public Integer getDay() {
        return Integer.parseInt(this.components[1].substring(0, this.components[2].length() - 1));
    }

    public Integer getYear() {
        return Integer.parseInt(this.components[2]);
    }

    public Integer getHour() {
        return Integer.parseInt(this.components[3].split(":")[0]);
    }

    public Integer getMinute() {
        return Integer.parseInt(this.components[3].split(":")[1]);
    }

    public Integer getSecond() {
        return Integer.parseInt(this.components[3].split(":")[2]);
    }

    public Integer getAM() {
        if (this.components[4].equals("NACHM.")) {
            return 1;
        }
        return 0;
    }

    /**
     * Compares two MyDate objects
     * @param o
     * @return 0, if datetimes are equal. Could be the same postentry.
     * @return 1, if this postentry is newer
     * @return -1, if this postentry is older
     */
    @Override
    public int compareTo(Object o) {
        MyDate opposite = (MyDate) o;

        /**If ... then this is newer than opposite
         *
         */
        if(
                this.getYear() > opposite.getYear() ||
                this.getYear() == opposite.getYear() && this.getMonth() > opposite.getMonth() ||
                this.getYear() == opposite.getYear() && this.getMonth() == opposite.getMonth() && this.getDay() > opposite.getDay() ||
                this.getYear() == opposite.getYear() && this.getMonth() == opposite.getMonth() && this.getDay() == opposite.getDay() && this.getHour() > opposite.getHour() && this.getAM() >= opposite.getAM() ||
                this.getYear() == opposite.getYear() && this.getMonth() == opposite.getMonth() && this.getDay() == opposite.getDay() && this.getHour() == opposite.getHour() && this.getMinute() > opposite.getMinute() && this.getAM() >= opposite.getAM() ||
                this.getYear() == opposite.getYear() && this.getMonth() == opposite.getMonth() && this.getDay() == opposite.getDay() && this.getHour() == opposite.getHour() && this.getMinute() == opposite.getMinute() && this.getSecond() > opposite.getSecond()&& this.getAM() >= opposite.getAM()){
            return 1;
        }else if(this.getYear() == opposite.getYear() && this.getMonth() == opposite.getMonth() && this.getDay() == opposite.getDay() && this.getHour() == opposite.getHour() && this.getMinute() == opposite.getMinute() && this.getSecond() == opposite.getSecond() && this.getAM() == opposite.getAM()){
            return 0;
        }
        return -1;
    }
}
