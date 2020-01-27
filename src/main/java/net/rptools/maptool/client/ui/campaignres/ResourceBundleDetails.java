/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.maptool.client.ui.campaignres;

public class ResourceBundleDetails {

  private String bundleName;
  private String qualifiedName;
  private String shortDescription;
  private String longDescription;
  private String bundleNameError;
  private String qualifiedNameError;
  private String shortDescriptionError;

  public String getBundleName() {
    return bundleName;
  }

  public void setBundleName(String bundleName) {
    this.bundleName = bundleName;
  }

  public String getQualifiedName() {
    return qualifiedName;
  }

  public void setQualifiedName(String qualifiedName) {
    this.qualifiedName = qualifiedName;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getLongDescription() {
    return longDescription;
  }

  public void setLongDescription(String longDescription) {
    this.longDescription = longDescription;
  }

  public String getBundleNameError() {
    return bundleNameError;
  }

  public void setBundleNameError(String bundleNameError) {
    this.bundleNameError = bundleNameError;
  }

  public String getQualifiedNameError() {
    return qualifiedNameError;
  }

  public void setQualifiedNameError(String qualifiedNameError) {
    this.qualifiedNameError = qualifiedNameError;
  }

  public String getShortDescriptionError() {
    return shortDescriptionError;
  }

  public void setShortDescriptionError(String shortDescriptionError) {
    this.shortDescriptionError = shortDescriptionError;
  }
}
