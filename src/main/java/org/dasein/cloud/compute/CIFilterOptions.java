package org.dasein.cloud.compute;

import org.dasein.cloud.CloudProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * Filtering options for filtering listings of composite infrastructures.
 * <p>Created by George Reese: 6/2/13 7:34 PM</p>
 * @author George Reese
 * @version 2013.07 initial version
 * @since 2013.07
 */
public class CIFilterOptions {

    /**
     * Constructs a filter for any kind of composite infrastructures.
     * @return a simple filter for composite infrastructures that does no filtering unless other options are added
     */
    static public @Nonnull CIFilterOptions getInstance() {
        return new CIFilterOptions(null, false);
    }

    /**
     * Constructs a filter for any kind of composite infrastructures.
     * @param matchesAny <code>true</code> if it is sufficient that just one of the criteria are matched, false if all are needed to be matched
     * @return a simple filter for composite infrastructures that does no filtering unless other options are added
     */
    static public @Nonnull CIFilterOptions getInstance(boolean matchesAny) {
        return new CIFilterOptions(null, matchesAny);
    }

    /**
     * Constructs a regex filter on the specified regular expression.
     * @param regex the regular expression on which to filter
     * @return a filter for composite infrastructures that match the specified regular expression
     */
    static public @Nonnull CIFilterOptions getInstance(@Nonnull String regex) {
        CIFilterOptions options = new CIFilterOptions(null, false);

        options.regex = regex;
        return options;
    }

    /**
     * Constructs a regex filter on the specified regular expression.
     * @param matchesAny <code>true</code> if it is sufficient that just one of the criteria are matched, false if all are needed to be matched
     * @param regex the regular expression on which to filter
     * @return a filter for composite infrastructures that match the specified regular expression
     */
    static public @Nonnull CIFilterOptions getInstance(boolean matchesAny, @Nonnull String regex) {
        CIFilterOptions options = new CIFilterOptions(null, matchesAny);

        options.regex = regex;
        return options;
    }

    private String             accountNumber;
    private boolean            matchesAny;
    private String             regex;
    private Map<String,String> tags;

    private CIFilterOptions(@Nullable String account, boolean matchesAny) {
        this.accountNumber = account;
        this.matchesAny = matchesAny;
    }

    /**
     * Matches a composite infrastructure against the criteria in this set of filter options.
     * @param ci the composite infrastructure to test
     * @return true if the composite infrastructure matches all criteria
     */
    public boolean matches(@Nonnull CompositeInfrastructure ci) {
        if( accountNumber != null ) {
            if( !accountNumber.equals(ci.getProviderOwnerId()) ) {
                if( !matchesAny ) {
                    return false;
                }
            }
            else if( matchesAny ) {
                return true;
            }
        }
        if( regex != null ) {
            boolean matches = (ci.getName().matches(regex) || ci.getDescription().matches(regex));

            if( !matches ) {
                for( Map.Entry<String,String> tag : ci.getTags().entrySet() ) {
                    String value = tag.getValue();

                    if( value != null && value.matches(regex) ) {
                        matches = true;
                        break;
                    }
                }
            }
            if( !matches && !matchesAny ) {
                return false;
            }
            else if( matches && matchesAny ) {
                return true;
            }
        }
        if( tags != null && !tags.isEmpty() ) {
            if( !CloudProvider.matchesTags(ci.getTags(), ci.getName(), ci.getDescription(), tags) ) {
                if( !matchesAny ) {
                    return false;
                }
            }
            else if( matchesAny ) {
                return true;
            }
        }
        return !matchesAny;
    }

    /**
     * Sets the tags to filter against.
     * @param tags the tags to filter against
     * @return this
     */
    public @Nonnull CIFilterOptions withTags(@Nonnull Map<String, String> tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public @Nonnull String toString() {
        return ("[" + (matchesAny ? "Match ANY: " : "Match ALL: ") + "accountNumber=" + accountNumber + ",regex=" + regex + "]");
    }
}
