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

package apis.models;
/*
 *
 * @author Mario Teklic
 */

import java.util.Date;

public class MyDate implements Comparable<MyDate>{

    private Date date;

    public MyDate(Date date) {
        this.date = new Date(date.getTime() * 1000);
    }

    public long getTime(){
        return this.date.getTime();
    }

    public String toString() {
        return date.toString();
    }

    /**
     * Compares two MyDate objects
     * @return 0, if datetimes are equal. Could be the same postentry.
     * @return 1, if this postentry is newer
     * @return -1, if this postentry is older
     */
    @Override
    public int compareTo(MyDate opposite) {
        return Long.valueOf(this.getTime()).compareTo(opposite.getTime());
    }
}
