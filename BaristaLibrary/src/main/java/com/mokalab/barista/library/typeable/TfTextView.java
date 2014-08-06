package com.mokalab.barista.library.typeable;

/*******************************************************************************
 * Copyright 2014 MokaLab.
 *
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
 *******************************************************************************/

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mokalab.barista.library.R;
import com.mokalab.butler.interfaces.ITypeFaceStyleable;
import com.mokalab.butler.util.TypefaceHelper;

/**
 * TextView with the ability to work with custom fonts.
 *
 * <br><br>
 * Author: Pirdad S.
 */
public class TfTextView extends TextView implements ITypeFaceStyleable {

    /**
     * Constructs TextView with custom Type Face.
     */
    public TfTextView(Context context) {
        this(context, null, 0);
    }

    /**
     * Constructs TextView with custom Type Face.
     */
    public TfTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructs TextView with custom Type Face.
     */
    public TfTextView(final Context context, final AttributeSet attrs, final int defStyle) {

        super(context, attrs, defStyle);
        TypefaceHelper.initializeTypeFaceCache();
        TypefaceHelper.manageAttributes(this, attrs, this);
    }

    /**
     * Set the TypeFace.
     *
     * @return return false if couldn't set the type face otherwise true.
     */
    public boolean setTypeFaceName(String typeFaceName) {

        if (TextUtils.isEmpty(typeFaceName)) return false;
        return TypefaceHelper.setTypeFace(this, typeFaceName);
    }

    @Override
    public int[] getTypeFaceStyleableAttrRes() {
        return R.styleable.TypefaceViewAttrs;
    }

    @Override
    public int getTypeFaceNameStyleable() {
        return R.styleable.TypefaceViewAttrs_typeFace;
    }
}
