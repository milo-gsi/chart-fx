package de.gsi.dataset;

import java.util.function.IntToDoubleFunction;

/**
 * Interface for accessing data on a cartesian grid.
 * 
 * @author Alexander Krimm
 */
public interface GridDataSet extends DataSet {
    
    /**
     * @return the shape of the grid of the data, e.g [3, 4] for a 3 x 4 matrix.
     */
    public int[] getShape();
    
    /**
     * Returns the Grid Value along the specified Grid dimension
     * 
     * @param dimIndex Dimension Index, smaller than getShape().size
     * @param index Index along the specified dimension, smaller than getShape()[dimIndex]
     * @return the value for the specified index on the grid along the specified dimension
     */
    public double getGrid(final int dimIndex, final int index);
    
    /**
     * @param dimIndex Dimension Index, smaller than getShape().size
     * @return A double[getShape()[dimIndex]] array containing all the grid values along the specified dimension
     */
    public default double[] getGridValues(final int dimIndex) {
        int n = getShape()[dimIndex];
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = getGrid(dimIndex, i);
        }
        return result;
    }
    
    /**
     * @param dimIndex Dimension index, smaller than getShape().size
     * @param x value along the specified axis to get the next index for
     * @return index which corresponds to the given value
     */
    public default int getGridIndex(final int dimIndex, final double x) {
        if (dimIndex > getShape().length) {
            throw new IndexOutOfBoundsException("dim index out of bounds");
        }
        if (getShape()[dimIndex] == 0) {
            return 0;
        }

        if (!Double.isFinite(x)) {
            return 0;
        }

        if (x <= this.getAxisDescription(dimIndex).getMin()) {
            return 0;
        }

        final int lastIndex = getShape()[dimIndex] - 1;
        if (x >= this.getAxisDescription(dimIndex).getMax()) {
            return lastIndex;
        }

        // binary closest search -- assumes sorted data set
        return binarySearchGrid(x, 0, lastIndex, i -> getGrid(dimIndex, i));
    }
    
    /**
     * @param dimIndex dimension to retrieve
     * @param indices indices to retrieve, missing indices are treated as zero
     * @return the value for the given indices
     */
    public double get(final int dimIndex, int ... indices);
    
    // get storage model information dimension ordering/strides/offsets + rawData access.
    // get subset of data, eg specific tile.
    
    /**
     * Updates the data of the GridDataSet with the data from the supplied dataset.
     * This is an optional function and by default returns an {@link UnsupportedOperationException}.
     * 
     * @param other dataset to copy data from.
     * @throws UnsupportedOperationException If the dataSet does not implement this setter
     * @throws IllegalArgumentException If the data is not compatible with this dataset's dimensions
     */
    public default void set(final GridDataSet other) {
        throw new UnsupportedOperationException("This Dataset does not implement updating data");
    }

    /**
     * Updates the data of the GridDataSet with the supplied data.
     * This is an optional function and by default returns an {@link UnsupportedOperationException}.
     * 
     * @param grid values for the grid
     * @param values values for the data
     * @throws UnsupportedOperationException If the dataSet does not implement this setter
     * @throws IllegalArgumentException If the data is not compatible with this dataset's dimensions
     */
    public default void set(final double[][] grid, final double[]... values) {
        throw new UnsupportedOperationException("This Dataset does not implement updating data");
    }

    private int binarySearchGrid(final double search, final int indexMin, final int indexMax, IntToDoubleFunction getter) {
        if (indexMin == indexMax) {
            return indexMin;
        }
        if (indexMax - indexMin == 1) {
            if (Math.abs(getter.applyAsDouble(indexMin) - search) < Math.abs(getter.applyAsDouble(indexMax) - search)) {
                return indexMin;
            }
            return indexMax;
        }
        final int middle = (indexMax + indexMin) / 2;
        final double valMiddle = getter.applyAsDouble(middle);
        if (valMiddle == search) {
            return middle;
        }
        if (search < valMiddle) {
            return binarySearchGrid(search, indexMin, middle, getter);
        }
        return binarySearchGrid(search, middle, indexMax, getter);
    }
}