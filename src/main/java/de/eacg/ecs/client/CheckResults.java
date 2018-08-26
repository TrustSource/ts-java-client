package de.eacg.ecs.client;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class CheckResults {

    public static class Warning {
        private String component;
        private String version;
        private String status;

        public Warning() {}

        public String getComponent() {
            return component;
        }

        public String getVersion() {
            return version;
        }

        public String getStatus() {
            return status;
        }

        public boolean isComponentNotFound() {
            return "component_not_found".equals(status);
        }

        public boolean isVersionNotFound() {
            return "version_not_found".equals(status);
        }

        public boolean isLicenseNotFound() {
            return "license_not_found".equals(status);
        }
    }

    public static class Result {
        private Component component;
        private String license;

        private LegalAnalysis changed;
        private LegalAnalysis not_changed;

        private List<Vulnerabilities> vulnerabilities;

        public Result(){}

        public Component getComponent() {
            return component;
        }

        public String getLicense() {
            return license;
        }

        public LegalAnalysis getChanged() {
            return changed;
        }

        public LegalAnalysis getNot_changed() {
            return not_changed;
        }

        public List<Vulnerabilities> getVulnerabilities() {
            return vulnerabilities;
        }
    }


    public static class Vulnerabilities {
        private String name;
        private String description;
        private String status;

        public Vulnerabilities() {}

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getStatus() {
            return status;
        }

        public boolean isWarning() {
            return status.equals("warnings");
        }

        public boolean isViolation() {
            return status.equals("violations");
        }
    }

    public static class LegalAnalysis {
        private List<Obligation> obligations;
        private List<Violation> violations;
        private ComputedStatus computedStatus;

        public LegalAnalysis(){
            this.obligations = new ArrayList<>();
        }

        public List<Obligation> getObligations() {
            return obligations;
        }

        public List<Violation> getViolations() {
            return violations;
        }

        public ComputedStatus getComputedStatus() {
            return computedStatus;
        }
    }

    public static class Obligation {
        private String type;
        private String message;


        public Obligation() { }

        public String getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }

    }

    public static class Violation {
        private String type;
        private String message;


        public Violation() { }

        public String getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }

        public boolean isWarning() {
            return type.equals("warning");
        }

        public boolean isViolation() {
            return type.equals("violation");
        }
    }

    public static class ComputedStatus {
        private Boolean requireWhitelist;
        private String status;
        private String label;
        private Boolean privateLicense;

        public ComputedStatus() {}

        public Boolean getRequireWhitelist() {
            return requireWhitelist;
        }

        public String getStatus() {
            return status;
        }

        public String getLabel() {
            return label;
        }

        public Boolean getPrivateLicense() {
            return privateLicense;
        }
    }

    private boolean ok;

    private List<Warning> warnings;
    private List<Result> data;

    public CheckResults(){}

    public boolean isOk() {
        return ok;
    }

    public List<Warning> getWarnings() {
        return warnings;
    }

    public List<Result> getData() {
        return data;
    }
}
