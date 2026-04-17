# ![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.pde.runtime/icons/obj16/package_obj@2x.png) libraries


## ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/ModelInformation.gif) ModelInformation


Common Types: 



**Specific fields**

|Reference|Type|Range|Description|
|--|--|--|--|
|ownedReferences|![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/LibraryReference.gif) [LibraryReference](https://github.com/eclipse/capella/wiki/Metamodel-libraries#-libraryreference)|0..*|  |
|version|![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/ModelVersion.gif) [ModelVersion](https://github.com/eclipse/capella/wiki/Metamodel-libraries#-modelversion)|0..1|  |


**Inherited fields** *(from ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [LibraryAbstractElement](https://github.com/eclipse/capella/wiki/Metamodel-libraries#-libraryabstractelement))*
|Attribute|Type|Range|Description|
|--|--|--|--|
|id|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EDataType.gif) [EString](https://github.com/eclipse/capella/wiki/Metamodel-ecore#-estring)|0..1| |


---
## ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/LibraryReference.gif) LibraryReference


Common Types: 



**Specific fields**
|Attribute|Type|Range|Description|
|--|--|--|--|
|accessPolicy|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EEnum.gif) [AccessPolicy](https://github.com/eclipse/capella/wiki/Metamodel-libraries#-accesspolicy)|1..1| 


|Reference|Type|Range|Description|
|--|--|--|--|
|library|![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/ModelInformation.gif) [ModelInformation](https://github.com/eclipse/capella/wiki/Metamodel-libraries#-modelinformation)|1..1|  |
|version|![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/ModelVersion.gif) [ModelVersion](https://github.com/eclipse/capella/wiki/Metamodel-libraries#-modelversion)|0..1|  |


**Inherited fields** *(from ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [LibraryAbstractElement](https://github.com/eclipse/capella/wiki/Metamodel-libraries#-libraryabstractelement))*
|Attribute|Type|Range|Description|
|--|--|--|--|
|id|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EDataType.gif) [EString](https://github.com/eclipse/capella/wiki/Metamodel-ecore#-estring)|0..1| |


---
## ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/ModelVersion.gif) ModelVersion


Common Types: 



**Specific fields**
|Attribute|Type|Range|Description|
|--|--|--|--|
|lastModifiedFileStamp|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EDataType.gif) [ELong](https://github.com/eclipse/capella/wiki/Metamodel-ecore#-elong)|0..1| 
|majorVersionNumber|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EDataType.gif) [EInt](https://github.com/eclipse/capella/wiki/Metamodel-ecore#-eint)|1..1| 
|minorVersionNumber|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EDataType.gif) [EInt](https://github.com/eclipse/capella/wiki/Metamodel-ecore#-eint)|1..1| 



**Inherited fields** *(from ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [LibraryAbstractElement](https://github.com/eclipse/capella/wiki/Metamodel-libraries#-libraryabstractelement))*
|Attribute|Type|Range|Description|
|--|--|--|--|
|id|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EDataType.gif) [EString](https://github.com/eclipse/capella/wiki/Metamodel-ecore#-estring)|0..1| |


---
## ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EEnum.gif) AccessPolicy


Common Types: 


|Attribute|Description|
|--|--|
|readAndWrite|
|readOnly|



---
## ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) *LibraryAbstractElement*


Common Types: 

Known Sub Types: ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/ModelInformation.gif) [ModelInformation](https://github.com/eclipse/capella/wiki/Metamodel-libraries#-modelinformation), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/LibraryReference.gif) [LibraryReference](https://github.com/eclipse/capella/wiki/Metamodel-libraries#-libraryreference), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/ModelVersion.gif) [ModelVersion](https://github.com/eclipse/capella/wiki/Metamodel-libraries#-modelversion)

**Specific fields**
|Attribute|Type|Range|Description|
|--|--|--|--|
|id|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EDataType.gif) [EString](https://github.com/eclipse/capella/wiki/Metamodel-ecore#-estring)|0..1| 



---

