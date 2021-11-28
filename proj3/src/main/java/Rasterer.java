import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {

        // params: lrlon, ullon, w, h, ullat, lrlat
        Map<String, Object> results = new HashMap<>();
        double ullat = params.get("ullat");
        if (params.get("ullat") > MapServer.ROOT_ULLAT) {
            ullat = MapServer.ROOT_ULLAT;
        }
        double ullon = params.get("ullon");
        if (params.get("ullon") < MapServer.ROOT_ULLON) {
            ullat = MapServer.ROOT_ULLON;
        }
        double lrlat = params.get("lrlat");
        if (params.get("lrlat") < MapServer.ROOT_LRLAT) {
            lrlat = MapServer.ROOT_LRLAT;
        }
        double lrlon = params.get("lrlon");
        if (params.get("lrlon") > MapServer.ROOT_LRLON) {
            lrlon = MapServer.ROOT_LRLON;
        }
        // check if ullon and ullat are within the root lon and lat
        if (!validateParams(ullat, lrlat, ullon, lrlon)) {
            results.put("render_grid", null);
            results.put("roster_ul_lon", 0);
            results.put("roster_ul_lat", 0);
            results.put("roster_lr_lon", 0);
            results.put("roster_lr_lat", 0);
            results.put("depth", 0);
            results.put("query_success", false);
            return results;
        }
        // calculate user LDPP
        double userLDPP = (lrlon - ullon) / params.get("w");
        // compare with LDPP of tiles
        // ldpp of 0th level of zoom
        double currLDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / 256;
        int depth = 0;
        boolean foundRightTiles = false;
        while (!foundRightTiles) {
            if (currLDPP <= userLDPP || depth == 7) {
                foundRightTiles = true;
                break;
            }
            // ldpp of next level of zoom;
            currLDPP = currLDPP / 2;
            depth++;
        }
        double lonIntervalDist = (MapServer.ROOT_ULLON - MapServer.ROOT_LRLON) / Math.pow(2, depth);
        double latIntervalDist = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);

        int startX = (int) ((MapServer.ROOT_ULLON - ullon) / lonIntervalDist);
        int startY = (int) ((MapServer.ROOT_ULLAT - ullat) / latIntervalDist);
        int endX = (int) ((MapServer.ROOT_ULLON - lrlon) / lonIntervalDist);
        int endY = (int) ((MapServer.ROOT_ULLAT - lrlat) / latIntervalDist);

        int numRows = endY - startY + 1;
        int numCols = endX - startX + 1;
        String[][] grid = new String[numRows][numCols];
        for (int y = 0; y < numRows; y++) {
            for (int x = 0; x < numCols; x++) {
                grid[y][x] = "d" + depth + "_x" + (x + startX) + "_y" + (y + startY) + ".png";
            }
        }
        double rasterULLon = MapServer.ROOT_ULLON - lonIntervalDist * startX;
        double rasterULLat = MapServer.ROOT_ULLAT - latIntervalDist * startY;
        double rasterLRLon = rasterULLon - lonIntervalDist * numCols;
        double rasterLRLat = rasterULLat - latIntervalDist * numRows;

        results.put("render_grid", grid);
        results.put("raster_ul_lon", rasterULLon);
        results.put("raster_ul_lat", rasterULLat);
        results.put("raster_lr_lon", rasterLRLon);
        results.put("raster_lr_lat", rasterLRLat);
        results.put("depth", depth);
        results.put("query_success", true);
        return results;
    }
    private boolean validateParams(double ullat, double lrlat, double ullon, double lrlon) {
        //ullat = 37.89, lrlat = 37.82 -> to the left means ullat > lrlat
        //ullon = -122.29, lrlon = -122.21 -> to the left means ullon < lrlon

        //check that ullat, ullon are in range
        //check that ullat and ullon are to the left of lrlat and lrlon
        if (ullat >= MapServer.ROOT_LRLAT && ullat <= MapServer.ROOT_ULLAT) {
            if (ullon >= MapServer.ROOT_ULLON && ullon <= MapServer.ROOT_LRLON) {
                return ullat >= lrlat && ullon <= lrlon;
            }
        }
        return false;
    }
}
