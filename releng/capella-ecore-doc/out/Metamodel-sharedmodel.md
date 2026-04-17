# ![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.pde.runtime/icons/obj16/package_obj@2x.png) sharedmodel
Shared aims at defining a structure for the shared model elements storage. It is dedicated to the model elements reuse. This is a rest of MDSysE and may be reengineered into more adapted solutions.

## ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/SharedPkg.gif) SharedPkg

[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.jdt.ui/icons/full/obj16/annotation_obj.png)](# "semantic")

a container specialized to hold elements intended to be shared across projects/analysis

Common Types: ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [Namespace](https://github.com/eclipse/capella/wiki/Metamodel-capellacore#-namespace),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [NamedElement](https://github.com/eclipse/capella/wiki/Metamodel-capellacore#-namedelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractNamedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstractnamedelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [ModelElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-modelelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [CapellaElement](https://github.com/eclipse/capella/wiki/Metamodel-capellacore#-capellaelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [TraceableElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-traceableelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [PublishableElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-publishableelement)



**Specific fields**

|Reference|Type|Range|Description|
|--|--|--|--|
|ownedDataPkg|![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/DataPkg.gif) [DataPkg](https://github.com/eclipse/capella/wiki/Metamodel-information#-datapkg)|0..1|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#owneddatapkg "the data packages contained in this shared package")  |
|ownedGenericPkg|![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/GenericPkg.gif) [GenericPkg](https://github.com/eclipse/capella/wiki/Metamodel-sharedmodel#-genericpkg)|0..1|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#ownedgenericpkg "the generic packages contained in this shared package")  |


**Inherited fields** *(from ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [ReuseableStructure](https://github.com/eclipse/capella/wiki/Metamodel-capellacore#-reuseablestructure),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [Structure](https://github.com/eclipse/capella/wiki/Metamodel-capellacore#-structure),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [ModelRoot](https://github.com/eclipse/capella/wiki/Metamodel-capellamodeller#-modelroot))*

|Reference|Type|Range|Description|
|--|--|--|--|
|ownedPropertyValuePkgs|![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/PropertyValuePkg.gif) [PropertyValuePkg](https://github.com/eclipse/capella/wiki/Metamodel-capellacore#-propertyvaluepkg)|0..*|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#ownedpropertyvaluepkgs "Link to packages that contain light extensions property values")  [![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.jdt.ui/icons/full/obj16/annotation_obj.png)](# "semantic")|

---
## ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/GenericPkg.gif) GenericPkg

a generic container for Capella elements and sub packages

Common Types: ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [Namespace](https://github.com/eclipse/capella/wiki/Metamodel-capellacore#-namespace),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [NamedElement](https://github.com/eclipse/capella/wiki/Metamodel-capellacore#-namedelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractNamedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstractnamedelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [ModelElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-modelelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [CapellaElement](https://github.com/eclipse/capella/wiki/Metamodel-capellacore#-capellaelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [TraceableElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-traceableelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [PublishableElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-publishableelement)



**Specific fields**

|Reference|Type|Range|Description|
|--|--|--|--|
|capellaElements|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [CapellaElement](https://github.com/eclipse/capella/wiki/Metamodel-capellacore#-capellaelement)|0..*|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#capellaelements "the Capella model elements stored directly under this package")  |
|subGenericPkgs|![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/GenericPkg.gif) [GenericPkg](https://github.com/eclipse/capella/wiki/Metamodel-sharedmodel#-genericpkg)|0..*|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#subgenericpkgs "sub-packages contained in this package")  |


**Inherited fields** *(from ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [Structure](https://github.com/eclipse/capella/wiki/Metamodel-capellacore#-structure))*

|Reference|Type|Range|Description|
|--|--|--|--|
|ownedPropertyValuePkgs|![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/PropertyValuePkg.gif) [PropertyValuePkg](https://github.com/eclipse/capella/wiki/Metamodel-capellacore#-propertyvaluepkg)|0..*|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#ownedpropertyvaluepkgs "Link to packages that contain light extensions property values")  [![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.jdt.ui/icons/full/obj16/annotation_obj.png)](# "semantic")|

---

