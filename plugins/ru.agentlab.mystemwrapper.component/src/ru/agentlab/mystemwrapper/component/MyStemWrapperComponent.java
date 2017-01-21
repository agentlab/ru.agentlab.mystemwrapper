/**
 *
 */
package ru.agentlab.mystemwrapper.component;

import ru.agentlab.mystemwrapper.StemmerWrapper;
import ru.agentlab.mystemwrapper.service.IMyStemWrapperService;

/**
 * @author admin
 *
 */
public class MyStemWrapperComponent
    implements IMyStemWrapperService {

    @Override
    public StemmerWrapper getStemmerWrapper() {
        return StemmerWrapper.getInstance();
    }

}
