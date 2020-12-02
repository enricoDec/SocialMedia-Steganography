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

package persistence;

import apis.reddit.Reddit;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public class PersistenceDummy implements JSONPersistentHelper{
    private static final Logger logger = Logger.getLogger(PersistenceDummy.class.getName());
    private String pathname = "jsonTest3.json";

    @Override
    public void writeToJsonFile(String jsonString) throws IOException {
        File file = new File(pathname);
        if(!file.exists()){
            if(file.createNewFile()){
                logger.info("Persistence file created.");
            }
        }

        FileUtils.writeStringToFile(file, jsonString, "UTF-8");
    }

    @Override
    public String readFromJsonFile() throws IOException {
        File file = new File(pathname);
        return FileUtils.readFileToString(file, "UTF-8");
    }
}
