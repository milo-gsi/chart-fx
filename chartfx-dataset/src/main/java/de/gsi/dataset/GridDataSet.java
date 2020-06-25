package de.gsi.dataset;

/**
 * Interface for accessing data on a cartesian grid.
 * 
 * @author Alexander Krimm
 */
public interface GridDataSet extends DataSet {
    
    /**
     * @return the shape of the grid of the data, e.g [3, 4] for a 3 x 4 matrix.
     */
    int[] getShape();
    
    /**
     * Returns the Grid Value along the specified Grid dimension
     * 
     * @param dimIndex Dimension Index, smaller than getShape().size
     * @param index Index along the specified dimension, smaller than getShape()[dimIndex]
     * @return the value for the specified index on the grid along the specified dimension
     */
    double getGrid(final int dimIndex, final int index);
    
    /**
     * @param dimIndex Dimension Index, smaller than getShape().size
     * @return A double[getShape()[dimIndex]] array containing all the grid values along the specified dimension
     */
    default double[] getGridValues(final int dimIndex) {
        int n = getShape()[dimIndex];
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = getGrid(dimIndex, i);
        }
        return result;
    }
    
    /**
     * @param dimIndex dimension to retrieve
     * @param indices indices to retrieve, missing indices are treated as zero
     * @return the value for the given indices
     */
    double get(final int dimIndex, int ... indices);
    
    // get storage model information dimension ordering/strides/offsets + rawData access.
    // get subset of data, eg specific tile.

}