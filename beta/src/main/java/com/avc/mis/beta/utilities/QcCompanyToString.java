/**
 * 
 */
package com.avc.mis.beta.utilities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.avc.mis.beta.entities.enums.QcCompany;

/**
 * @author Zvi
 *
 */
@Converter(autoApply = true)
public class QcCompanyToString implements AttributeConverter<QcCompany, String> {

	@Override
    public String convertToDatabaseColumn(QcCompany value) {
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    @Override
    public QcCompany convertToEntityAttribute(String label) {
        if (label != null) {
            return QcCompany.valueOfLabel(label);
        }
        return null;
    }
}
