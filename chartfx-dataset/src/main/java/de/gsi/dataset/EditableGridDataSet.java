package de.gsi.dataset;

/**
 * Interface for an editable GridDataSet. Allows adding/removing/setting data.
 *
 * @author Alexander Krimm
 */
public interface EditableGridDataSet extends GridDataSet {
    /**
     * @param name the new data set name
     * @return itself (fluent design)
     */
    EditableGridDataSet setName(final String name);
    
//    /**
//     * 
//     * @return edit constraints for data set
//     */
//    GridEditConstraints getEditConstraints();
//    
//    /**
//     * 
//     * @param constraints new edit constraints
//     * @return itself (fluent design)
//     */
//    EditableDataSet setEditConstraints(final GridEditConstraints constraints);
    
    /**
     * Deletes a row/plane/... in the dataset.
     * 
     * @param dimIndex along which dimension to delete index
     * @param index index to delete
     * @return itself for method chaining
     */
    EditableGridDataSet delete(int dimIndex, int index);
    
    /**
     * Sets a single value in the dataSet
     * @param dimIndex dim index to set, must be greater than nGrid
     * @param indices indices of the point to set
     * @param value value to set
     * @return itself for method chaining
     */
    EditableGridDataSet set(int dimIndex, int[] indices, double value);
    
    /**
     * Sets the values for a row/plane/... in the dataset.
     * 
     * @param dimIndex dimension to set value in
     * @param indices select indices to set. Indices equal to -1 will be set
     * @param values values to set, length must be equal to product of shape[i] for all indices[i] == -1
     * @return itself for method chaining
     */
    EditableGridDataSet set(int dimIndex, int[] indices, double[] values);
    
    /**
     * Add a new row/plane/... to the dataset.
     * 
     * @param dimIndex dimension to insert the new values into
     * @param index index after which to add the new value
     * @param value the new grid value
     * @param values the values for the row/plane/... Length must be product of all grid dimensions except dimIndex
     * @return itself for method chaining
     */
    EditableGridDataSet add(int dimIndex, int index, double value, double[] values);
}