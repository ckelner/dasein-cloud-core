/**
 * Copyright (C) 2009-2013 enstratius, Inc.
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.dasein.cloud.storage;

import javax.annotation.Nullable;

public abstract class AbstractStorageServices implements StorageServices {
    @Override
    public @Nullable BlobStoreSupport getBlobStoreSupport() {
        return null;
    }

    @Override
    public boolean hasBlobStoreSupport() {
        return (getBlobStoreSupport() != null);
    }

}
